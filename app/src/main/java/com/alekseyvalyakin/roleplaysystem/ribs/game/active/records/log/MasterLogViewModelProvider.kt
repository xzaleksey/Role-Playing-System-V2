package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.log

import com.alekseyvalyakin.roleplaysystem.base.filter.FilterModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.log.LogRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.log.MessageType
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.flexible.secondarysubheader.SecondarySubHeaderViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.log.adapter.LogItemTextViewModel
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.Flowable
import io.reactivex.rxkotlin.Flowables
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class LogViewModelProviderImpl(
        private val game: Game,
        private val logRepository: LogRepository,
        private val stringRepository: StringRepository,
        private val filterModelFlowable: Flowable<FilterModel>
) : LogViewModelProvider {

    override fun observeViewModel(): Flowable<LogViewModel> {
        return Flowables.combineLatest(filterModelFlowable, logRepository.observeLogMessagesOrdered(game.id))
                .map { pair ->
                    val items = mutableListOf<IFlexible<*>>()
                    val messages = pair.second
                    var currentDate: DateTime? = null
                    val today = DateTime().withTimeAtStartOfDay()
                    messages.forEach { message ->
                        if (message.getMessageType() == MessageType.TEXT
                                && message.textMessage != null
                                && message.textMessage!!.isFiltered(pair.first.query)) {
                            val messageDate = DateTime(message.getDate()).withTimeAtStartOfDay()
                            if (messageDate != currentDate) {
                                currentDate = messageDate
                                val title = when {
                                    today == messageDate -> stringRepository.getToday()
                                    today.minusDays(1) == messageDate -> stringRepository.getYesterday()
                                    else -> messageDate.toString(DateTimeFormat.forPattern("d MMMM"))
                                }
                                items.add(SecondarySubHeaderViewModel(title))
                            }
                            items.add(LogItemTextViewModel(message.id, message.textMessage!!.text))
                        }
                    }
                    return@map LogViewModel(items)
                }
    }
}

interface LogViewModelProvider {
    fun observeViewModel(): Flowable<LogViewModel>
}