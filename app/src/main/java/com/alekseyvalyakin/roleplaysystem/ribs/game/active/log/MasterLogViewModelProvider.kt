package com.alekseyvalyakin.roleplaysystem.ribs.game.active.log

import com.alekseyvalyakin.roleplaysystem.base.filter.FilterModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.log.LogRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.log.MessageType
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.log.adapter.LogItemTextViewModel
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.Flowable
import io.reactivex.rxkotlin.Flowables
import org.jetbrains.anko.collections.forEachReversedByIndex
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class LogViewModelProviderImpl(
        private val game: Game,
        private val logRepository: LogRepository
) : LogViewModelProvider {

    override fun observeViewModel(filterModelFlowable: Flowable<FilterModel>): Flowable<LogViewModel> {
        return Flowables.combineLatest(filterModelFlowable, logRepository.observeLogMessagesOrdered(game.id))
                .map { pair ->
                    val items = mutableListOf<IFlexible<*>>()
                    val messages = pair.second
                    messages.forEachReversedByIndex { message ->
                        if (message.getMessageType() == MessageType.TEXT
                                && message.textMessage != null
                                && message.textMessage!!.isFiltered(pair.first.query)) {
                            items.add(LogItemTextViewModel(message.id,
                                    message.textMessage!!.text,
                                    DateTime(message.getDate().time).toString(DateTimeFormat.shortTime())))
                        }
                    }
                    return@map LogViewModel(items)
                }
    }
}

interface LogViewModelProvider {
    fun observeViewModel(filterModelFlowable: Flowable<FilterModel>): Flowable<LogViewModel>
}