package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio.AudioBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio.AudioRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.log.LogBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.log.LogRouter
import com.uber.rib.core.AttachInfo
import com.uber.rib.core.BaseRouter
import com.uber.rib.core.BaseSerializableRouterNavigatorState
import com.uber.rib.core.DefaultAttachTransition
import com.uber.rib.core.DefaultDetachTransition
import timber.log.Timber

/**
 * Adds and removes children of {@link PhotoBuilder.PhotoScope}.
 *
 */
class RecordsRouter(
        view: RecordsView,
        interactor: RecordsInteractor,
        component: RecordsBuilder.Component,
        logBuilder: LogBuilder,
        audioBuilder: AudioBuilder
) : BaseRouter<RecordsView, RecordsInteractor, RecordsRouter.State, RecordsBuilder.Component>(view, interactor, component) {

    private val logAttachTransition = object : DefaultAttachTransition<State, LogBuilder>(logBuilder, view.container) {}
    private val logDetachTransition = DefaultDetachTransition<State>(view.container)
    private val audioAttachTransition = object : DefaultAttachTransition<State, AudioBuilder>(audioBuilder, view.container) {}
    private val audioDetachTransition = DefaultDetachTransition<State>(view.container)

    fun attachLog() {
        attachRib(AttachInfo(State.LOG))
    }

    fun attachAudio() {
        attachRib(AttachInfo(State.AUDIO))
    }

    override fun attachRib(attachInfo: AttachInfo<State>):Boolean {
        Timber.d("attachRib %s", attachInfo.state)
        return when (attachInfo.state) {
            State.LOG -> {
                internalPushTransientState(attachInfo.state, logAttachTransition, logDetachTransition)
            }
            State.AUDIO -> {
                internalPushTransientState(attachInfo.state, audioAttachTransition, audioDetachTransition)
            }
        }
    }

    override fun hasOwnContent(): Boolean {
        return false
    }

    sealed class State : BaseSerializableRouterNavigatorState() {
        object LOG : State() {
            override fun name(): String = "LOG"
        }

        object AUDIO : State() {
            override fun name(): String = "AUDIO"
        }
    }
}

