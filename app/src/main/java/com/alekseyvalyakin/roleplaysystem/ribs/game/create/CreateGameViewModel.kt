package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import java.io.Serializable

data class CreateGameViewModel(
        val title: String,
        val stepText: String,
        val step: CreateGameStep,
        val inputText: String,
        val inputMaxLines: Int,
        val inputHint: String,
        val inputExample: String,
        val required: Boolean,
        val inputType: Int,
        val isDeleted: Boolean = false
) : Serializable {

    companion object {
        const val KEY = "CreateGameViewModel"
    }
}