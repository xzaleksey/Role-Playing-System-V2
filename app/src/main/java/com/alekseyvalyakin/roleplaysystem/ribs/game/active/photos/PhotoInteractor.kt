package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGame
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameDao
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.alekseyvalyakin.roleplaysystem.utils.createCompletable
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import io.reactivex.Scheduler
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
    lateinit var photoInGameViewModelProvider: PhotoViewModelProvider

    @field:[Inject ThreadConfig(ThreadConfig.TYPE.IO)]
    lateinit var ioScheduler: Scheduler

    @Inject
    lateinit var game: Game

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)


        photoInGameViewModelProvider.observeViewModel()
                .subscribeWithErrorLogging {
                    presenter.update(it)
                }
                .addToDisposables()

        //        photoInGameDao.all().subscribeWithErrorLogging { photos ->
//            photos.forEach {
//                Timber.d(it.toString())
//            }
//        }
    }

    override fun willResignActive() {
        super.willResignActive()
    }

    private fun createPhotoInGame(photoInGame: PhotoInGame) {
        createCompletable({ photoInGameDao.insert(photoInGame) }, ioScheduler)
                .subscribeWithErrorLogging()
    }

}


