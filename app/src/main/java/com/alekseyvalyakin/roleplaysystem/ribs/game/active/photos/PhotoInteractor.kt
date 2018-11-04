package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import com.alekseyvalyakin.roleplaysystem.data.firestorage.FirebaseStorageRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.FireStoreVisibility
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.PhotoInGameRepository
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameDao
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameUploadModel
import com.alekseyvalyakin.roleplaysystem.data.workmanager.WorkManagerWrapper
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameEvent
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto.FullSizePhotoModel
import com.alekseyvalyakin.roleplaysystem.utils.createCompletable
import com.alekseyvalyakin.roleplaysystem.utils.image.ImagesResult
import com.alekseyvalyakin.roleplaysystem.utils.image.LocalImageProvider
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.jakewharton.rxrelay2.Relay
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import timber.log.Timber
import javax.inject.Inject

/**
 * Coordinates Business Logic for [PhotoScope].
 *
 */
@RibInteractor
class PhotoInteractor : BaseInteractor<PhotoPresenter, PhotoRouter>() {

    @Inject
    lateinit var presenter: PhotoPresenter

    @Inject
    lateinit var photoInGameDao: PhotoInGameDao
    @Inject
    lateinit var photoInGameInGameViewModelProvider: PhotoInGameViewModelProvider
    @Inject
    lateinit var localImageProvider: LocalImageProvider
    @field:[Inject ThreadConfig(ThreadConfig.TYPE.IO)]
    lateinit var ioScheduler: Scheduler
    @field:[Inject ThreadConfig(ThreadConfig.TYPE.UI)]
    lateinit var uiScheduler: Scheduler
    @Inject
    lateinit var workManagerWrapper: WorkManagerWrapper
    @Inject
    lateinit var photoInGameRepository: PhotoInGameRepository
    @Inject
    lateinit var firebaseStorageRepository: FirebaseStorageRepository
    @Inject
    lateinit var activeGameEventRelay: Relay<ActiveGameEvent>
    @Inject
    lateinit var analyticsReporter: AnalyticsReporter
    private val screenName = "GamePhotos"

    @Inject
    lateinit var game: Game

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)

        observeCreatePhotoUpload()

        photoInGameInGameViewModelProvider.observeViewModel()
                .observeOn(uiScheduler)
                .subscribeWithErrorLogging {
                    presenter.update(it)
                }
                .addToDisposables()

        presenter.observeUiEvents()
                .flatMap(this::handleUiEvent)
                .onErrorReturn { Timber.e(it) }
                .subscribeWithErrorLogging()
                .addToDisposables()
    }

    private fun handleUiEvent(uiEvent: PhotoPresenter.UiEvent): Observable<*> {
        when (uiEvent) {
            is PhotoPresenter.UiEvent.ChoosePhoto -> {
                localImageProvider.pickImage()
            }
            is PhotoPresenter.UiEvent.SwitchVisibility -> {
                val model = uiEvent.photoFlexibleViewModel
                val fireStoreVisibility = if (!model.visible) FireStoreVisibility.VISIBLE_TO_ALL else FireStoreVisibility.HIDDEN
                analyticsReporter.logEvent(GamePhotoAnalyticsEvent.SwitchPhotoVisibility(game, model.id, fireStoreVisibility.value.toString()))
                return photoInGameRepository.switchVisibility(
                        game.id,
                        model.id,
                        fireStoreVisibility
                ).onErrorComplete().toObservable<Any>()
            }

            is PhotoPresenter.UiEvent.DeletePhoto -> {
                val model = uiEvent.photoFlexibleViewModel
                analyticsReporter.logEvent(GamePhotoAnalyticsEvent.DeletePhoto(game, model.id))
                return photoInGameRepository.deleteDocumentOffline(
                        game.id,
                        model.id
                ).andThen(firebaseStorageRepository.deletePhotoInGame(game.id, model.id))
                        .onErrorComplete {
                            Timber.e(it)
                            presenter.showError(it.localizedMessage)
                            true
                        }
                        .toObservable<Any>()
            }

            is PhotoPresenter.UiEvent.OpenFullSize -> {
                val photoFlexibleViewModel = uiEvent.photoFlexibleViewModel
                analyticsReporter.logEvent(GamePhotoAnalyticsEvent.OpenPhoto(game, photoFlexibleViewModel.id))
                activeGameEventRelay.accept(ActiveGameEvent.OpenFullSizePhoto(
                        FullSizePhotoModel(photoFlexibleViewModel.imageData,
                                photoFlexibleViewModel.name)
                ))
            }
            is PhotoPresenter.UiEvent.TitleChangeOpen -> {
                return Observable.fromCallable {
                    presenter.showChangeTitleDialog(uiEvent.photoFlexibleViewModel)
                }
            }
            is PhotoPresenter.UiEvent.EditNameConfirm -> {
                val photoFlexibleViewModel = uiEvent.photoFlexibleViewModel
                analyticsReporter.logEvent(GamePhotoAnalyticsEvent.ChangePhotoName(game, photoFlexibleViewModel.id))

                return photoInGameRepository.updateName(game.id,
                        photoFlexibleViewModel.id,
                        photoFlexibleViewModel.name)
                        .toObservable<Any>()
            }
        }
        return Observable.empty<Any>()
    }

    private fun observeCreatePhotoUpload() {
        localImageProvider.observeImage().flatMapCompletable {
            val completable = if (it is ImagesResult.Success) {
                createCompletable({
                    val photoInGame = PhotoInGameUploadModel(
                            gameId = game.id,
                            filePath = it.images.first().originalPath,
                            photoId = FirestoreCollection.PhotosInGame(game.id).getDbCollection().document().id
                    )
                    photoInGame.id = photoInGameDao.insert(photoInGame)
                    workManagerWrapper.startUploadPhotoInGameWork(photoInGame)
                    analyticsReporter.logEvent(GamePhotoAnalyticsEvent.UploadPhoto(game))
                }, ioScheduler)
            } else {
                Completable.error(RuntimeException((it as ImagesResult.Error).error))
            }
            return@flatMapCompletable completable.observeOn(uiScheduler)
                    .onErrorComplete { t ->
                        Timber.e(t)
                        presenter.showError(t.localizedMessage)
                        true
                    }
        }.subscribeWithErrorLogging {
            presenter.collapseFab()
        }.addToDisposables()
    }
}


