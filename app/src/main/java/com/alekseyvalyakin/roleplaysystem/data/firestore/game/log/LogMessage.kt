package com.alekseyvalyakin.roleplaysystem.data.firestore.game.log

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDateCreate
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class LogMessage(
        @ServerTimestamp
        override var dateCreate: Date? = null,
        var localDate: Date = Date(),
        var type: Int = MessageType.TEXT.value,
        var textMessage: TextMessage? = null,

        @Exclude
        @set:Exclude
        @get:Exclude
        override var id: String = StringUtils.EMPTY_STRING
) : FireStoreIdModel, HasDateCreate {

    @Exclude
    fun getMessageType(): MessageType {
        return MessageType.getTypeByValue(type)
    }

    @Exclude
    fun getDate(): Date {
        if (dateCreate != null) {
            return dateCreate!!
        }

        return localDate
    }

    companion object {
        fun createTextModel(text: String): LogMessage {
            return LogMessage(
                    type = MessageType.TEXT.value,
                    textMessage = TextMessage(text),
                    localDate = Date()
            )
        }
    }
}

data class TextMessage(
        var text: String = StringUtils.EMPTY_STRING
) : FireStoreModel

enum class MessageType(val value: Int) {
    UNKNOWN(0),
    TEXT(1);

    companion object {
        fun getTypeByValue(value: Int): MessageType {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }
    }
}
