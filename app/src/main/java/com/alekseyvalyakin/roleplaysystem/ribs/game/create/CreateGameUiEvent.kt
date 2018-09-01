package com.alekseyvalyakin.roleplaysystem.ribs.game.create

sealed class CreateGameUiEvent {
    class ClickNext(val text: String) : CreateGameUiEvent()
    class InputChange(val text: String) : CreateGameUiEvent()

    object DeleteGame : CreateGameUiEvent()
    object ConfirmDeleteGame : CreateGameUiEvent()
    object BackPress : CreateGameUiEvent()
}