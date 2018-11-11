package com.alekseyvalyakin.roleplaysystem.ribs.game.active.log

import com.alekseyvalyakin.roleplaysystem.data.sound.RecordInfo
import eu.davidea.flexibleadapter.items.IFlexible
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

data class LogViewModel(
        val items: List<IFlexible<*>>
)

data class LogRecordState(
        val recordInfo: RecordInfo = RecordInfo()
) {
    fun isShowMic(): Boolean {
        return !isShowRecordPlate()
    }

    fun isShowRecordPlate(): Boolean {
        return !recordInfo.isTempFileEmpty() && recordInfo.isFinalFileEmpty()
    }

    fun isInProgress() = recordInfo.inProgress

    fun getTimePassed(): String {
        return DateTime(recordInfo.timePassed)
                .withZone(DateTimeZone.UTC)
                .toString("HH:mm:ss")
    }

    fun isFinished(): Boolean = recordInfo.isRecordingFinished()
}