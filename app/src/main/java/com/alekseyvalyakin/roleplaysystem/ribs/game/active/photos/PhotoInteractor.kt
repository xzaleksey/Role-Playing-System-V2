package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import com.alekseyvalyakin.roleplaysystem.data.firestorage.FirebaseStorageRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.FireStoreVisibility
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.PhotoInGameRepository
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameDao
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameUploadModel
import com.alekseyvalyakin.roleplaysystem.data.workmanager.WorkManagerWrapper
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.alekseyvalyakin.roleplaysystem.utils.createCompletable
import com.alekseyvalyakin.roleplaysystem.utils.image.ImagesResult
import com.alekseyvalyakin.roleplaysystem.utils.image.LocalImageProvider
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
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
    lateinit var game: Game

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        observeCreatePhotoUpload()

        photoInGameInGameViewModelProvider.observeViewModel()
                .observeOn(uiScheduler)
                .subscribeWithErrorLogging {
                    presenter.update(it)
                }
                .addToDisposables()

        presenter.observeUiEvents()
                .flatMap(this::handleUiEvent)
                .subscribeWithErrorLogging {

                }
        photoInGameDao.all().subscribeWithErrorLogging { photos ->
            photos.forEach {
                Timber.d(it.toString())
            }
        }
    }

    private fun handleUiEvent(uiEvent: PhotoPresenter.UiEvent): Observable<*> {
        when (uiEvent) {
            is PhotoPresenter.UiEvent.FabClicked -> {
                localImageProvider.pickImage()
            }
            is PhotoPresenter.UiEvent.SwitchVisibility -> {
                val model = uiEvent.photoFlexibleViewModel
                return photoInGameRepository.switchVisibility(
                        game.id,
                        model.id,
                        if (!model.visible) FireStoreVisibility.VISIBLE_TO_ALL else FireStoreVisibility.HIDDEN
                ).onErrorComplete().toObservable<Any>()
            }

            is PhotoPresenter.UiEvent.DeletePhoto -> {
                val model = uiEvent.photoFlexibleViewModel
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


        }
        return Observable.empty<Any>()
    }

    private fun observeCreatePhotoUpload() {
        localImageProvider.observeImage().flatMapCompletable {
            val completable = if (it is ImagesResult.Success) {
                createCompletable({
                    val photoInGame = PhotoInGameUploadModel(
                            gameId = game.id,
                            filePath = it.images.first().originalPath
                    )
                    photoInGame.id = photoInGameDao.insert(photoInGame)
                    workManagerWrapper.startUploadPhotoInGameWork(photoInGame)
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
        }
                .subscribeWithErrorLogging()
                .addToDisposables()
    }

    override fun willResignActive() {
        super.willResignActive()
    }
}


