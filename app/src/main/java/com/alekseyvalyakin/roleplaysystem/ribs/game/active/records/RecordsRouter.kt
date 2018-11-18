package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio.AudioBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio.AudioRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.log.LogBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.log.LogRouter
import com.uber.rib.core.*
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

    private val logAttachTransition = object : DefaultAttachTransition<LogRouter, State, LogBuilder>(logBuilder, view.container) {}
    private val logDetachTransition = DefaultDetachTransition<LogRouter, State>(view.container)
    private val audioAttachTransition = object : DefaultAttachTransition<AudioRouter, State, AudioBuilder>(audioBuilder, view.container) {}
    private val audioDetachTransition = DefaultDetachTransition<AudioRouter, State>(view.container)

    fun attachLog() {
        attachRib(AttachInfo(State.LOG))
    }

    fun attachAudio() {
        attachRib(AttachInfo(State.AUDIO))
    }

    override fun attachRib(attachInfo: AttachInfo<State>) {
        Timber.d("attachRib %s", attachInfo.state)
        when (attachInfo.state) {
            State.LOG -> {
                pushTransientState(attachInfo.state, logAttachTransition, logDetachTransition)
            }
            State.AUDIO -> {
                pushTransientState(attachInfo.state, audioAttachTransition, audioDetachTransition)
            }
        }
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

