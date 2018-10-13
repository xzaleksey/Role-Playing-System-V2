package com.alekseyvalyakin.roleplaysystem.ribs.game.active.log

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import io.reactivex.Flowable

class LogViewModelProviderImpl(
        private val game: Game
) : LogViewModelProvider {

    override fun observeViewModel(): Flowable<LogViewModel> {
        return Flowable.empty()
    }
}

interface LogViewModelProvider {
    fun observeViewModel(): Flowable<LogViewModel>
}