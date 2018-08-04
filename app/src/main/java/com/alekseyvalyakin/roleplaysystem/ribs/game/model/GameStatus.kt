package com.alekseyvalyakin.roleplaysystem.ribs.game.model

enum class GameStatus(val value: Int) {
    DRAFT(1),
    ACTIVE(2),
    COMPLETED(3),
    UNKNOWN(Int.MAX_VALUE);

    companion object {
        fun getStatusByValue(value: Int): GameStatus {
            for (status in values()) {
                if (status.value == value) {
                    return status
                }
            }
            return UNKNOWN
        }
    }
}