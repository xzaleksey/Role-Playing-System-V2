package com.alekseyvalyakin.roleplaysystem.ribs.root

import com.alekseyvalyakin.roleplaysystem.data.auth.AuthProvider
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.User
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameListener
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameRouter
import com.alekseyvalyakin.roleplaysystem.ribs.main.MainRibListener
import com.alekseyvalyakin.roleplaysystem.ribs.profile.ProfileListener
import com.alekseyvalyakin.roleplaysystem.ribs.profile.ProfileRouter
import com.alekseyvalyakin.roleplaysystem.utils.image.LocalImageProvider
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.*
import io.reactivex.Observable
import io.reactivex.Scheduler
import timber.log.Timber
import java.io.Serializable
import javax.inject.Inject

/**
 * Coordinates Business Logic for [RootScope].
 *
 * Base router
 */
@RibInteractor
class RootInteractor : BaseInteractor<RootInteractor.RootPresenter, RootRouter>(), ProfileListener {
    @Inject
    lateinit var presenter: RootPresenter
    @Inject
    lateinit var authProvider: AuthProvider
    @field:[Inject ThreadConfig(ThreadConfig.TYPE.UI)]
    lateinit var uiScheduler: Scheduler
    @Inject
    lateinit var mainRibEventObservable: Observable<MainRibListener.MainRibEvent>
    @Inject
    lateinit var createGameObservable: Observable<CreateGameListener.CreateGameEvent>
    @Inject
    lateinit var localImageProvider: LocalImageProvider

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        authProvider.observeLoggedInState()
                .observeOn(uiScheduler)
                .subscribeWithErrorLogging { loggedIn ->
                    if (!loggedIn) {
                        router.attachAuth()
                    } else {
                        router.attachMain()
                    }
                }.addToDisposables()

        mainRibEventObservable
                .observeOn(uiScheduler)
                .subscribeWithErrorLogging { event ->
                    when (event) {
                        is MainRibListener.MainRibEvent.CreateGame -> {
                            router.attachCreateGame(event.game)
                        }
                        is MainRibListener.MainRibEvent.OpenActiveGame -> {
                            router.attachOpenActiveGame(event.game)
                        }
                        is MainRibListener.MainRibEvent.MyProfile -> {
                            router.attachMyProfile(event.user)
                        }
                    }
                }.addToDisposables()

        createGameObservable
                .observeOn(uiScheduler)
                .subscribeWithErrorLogging { event ->
                    when (event) {
                        is CreateGameListener.CreateGameEvent.CompleteCreate -> {
                            router.detachCreateGame()
                            router.attachOpenActiveGame(event.game)
                        }
                    }
                }.addToDisposables()

        localImageProvider.subscribe().addToDisposables()
    }

    override fun openGame(game: Game) {
        router.attachOpenActiveGame(game)
    }

    override fun handleBackPress(): Boolean {
        return router.onBackPressed()
    }

    override fun <T : Router<out Interactor<*, *>, out InteractorBaseComponent<*>>> restoreRouter(clazz: Class<T>, childInfo: Serializable?) {
        if (clazz == CreateGameRouter::class.java) {
            Timber.d("Restored create game Router")
            router.attachMain()
            router.attachCreateGame(childInfo as Game)
        }

        if (clazz == ActiveGameRouter::class.java) {
            Timber.d("Restored active game Router")
            router.attachMain()
            router.attachOpenActiveGame(childInfo as Game)
        }

        if (clazz == ProfileRouter::class.java) {
            Timber.d("Restored Profile Router")
            router.attachMain()
            router.attachMyProfile(childInfo as User)
        }
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface RootPresenter
}
