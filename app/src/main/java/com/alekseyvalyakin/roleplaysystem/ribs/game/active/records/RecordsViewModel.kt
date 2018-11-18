package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records

import com.alekseyvalyakin.roleplaysystem.data.sound.RecordInfo
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.io.Serializable

data class RecordsViewModel(
        val recordTab: RecordTab = RecordTab.LOG
) : Serializable

enum class RecordTab(val index: Int) {
    LOG(0),
    AUDIO(1)
}

data class RecordState(
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