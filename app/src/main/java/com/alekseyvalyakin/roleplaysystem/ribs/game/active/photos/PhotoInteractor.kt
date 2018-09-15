package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGame
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameDao
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.alekseyvalyakin.roleplaysystem.utils.createCompletable
import com.alekseyvalyakin.roleplaysystem.utils.image.ImagesResult
import com.alekseyvalyakin.roleplaysystem.utils.image.LocalImageProvider
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import io.reactivex.Completable
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
    lateinit var game: Game

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        observeCreatePhotoUpload()

        photoInGameInGameViewModelProvider.observeViewModel()
                .subscribeWithErrorLogging {
                    presenter.update(it)
                }
                .addToDisposables()

        presenter.observeUiEvents()
                .subscribeWithErrorLogging { event ->
                    when (event) {
                        is PhotoPresenter.UiEvent.FabClicked -> {
                            localImageProvider.pickImage()
                        }
                    }
                }

        photoInGameDao.all().subscribeWithErrorLogging { photos ->
            photos.forEach {
                Timber.d(it.toString())
            }
        }
    }

    private fun observeCreatePhotoUpload() {
        localImageProvider.observeImage().flatMapCompletable {
            val completable = if (it is ImagesResult.Success) {
                createCompletable({
                    photoInGameDao.insert(PhotoInGame(
                            gameId = game.id,
                            filePath = it.images.first().originalPath
                    ))
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

    private fun createPhotoInGame(photoInGame: PhotoInGame) {
        createCompletable({ photoInGameDao.insert(photoInGame) }, ioScheduler)
                .subscribeWithErrorLogging()
    }

}


