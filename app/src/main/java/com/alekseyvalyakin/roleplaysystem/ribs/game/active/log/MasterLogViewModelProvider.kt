package com.alekseyvalyakin.roleplaysystem.ribs.game.active.log

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.log.LogRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.log.MessageType
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.log.adapter.LogItemTextViewModel
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.Flowable
import org.jetbrains.anko.collections.forEachReversedByIndex
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class LogViewModelProviderImpl(
        private val game: Game,
        private val logRepository: LogRepository
) : LogViewModelProvider {

    override fun observeViewModel(): Flowable<LogViewModel> {
        return logRepository.observeLogMessagesOrdered(game.id)
                .map { messages ->
                    val items = mutableListOf<IFlexible<*>>()
                    messages.forEachReversedByIndex { message ->
                        if (message.getMessageType() == MessageType.TEXT && message.textMessage != null) {
                            items.add(LogItemTextViewModel(message.id,
                                    message.textMessage!!.text,
                                    DateTime(message.getDate().time).toString(DateTimeFormat.shortTime())))
                        }
                    }
                    LogViewModel(items)
                }
    }
}

interface LogViewModelProvider {
    fun observeViewModel(): Flowable<LogViewModel>
}