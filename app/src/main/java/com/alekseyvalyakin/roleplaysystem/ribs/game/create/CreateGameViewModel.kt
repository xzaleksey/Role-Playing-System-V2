package com.alekseyvalyakin.roleplaysystem.ribs.game.create

data class CreateGameViewModel(
        val title: String,
        val stepText: String,
        val step: CreateGameStep,
        val inputHint: String,
        val inputExample: String,
        val required: Boolean
)