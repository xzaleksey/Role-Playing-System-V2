package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import com.alekseyvalyakin.roleplaysystem.data.game.Game
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository

class CreateGameViewModelProvider constructor(
        private val stringRepository: StringRepository
) {
    private val maxSteps = 3

    fun getCreateGameViewModel(step: CreateGameStep, game: Game): CreateGameViewModel {
        when (step) {
            CreateGameStep.TITLE -> {
                return CreateGameViewModel(
                        stringRepository.createGameNameTitle(),
                        stringRepository.createGameStepText(CreateGameStep.TITLE.value, maxSteps),
                        CreateGameStep.TITLE,
                        game.name,
                        1,
                        stringRepository.name(),
                        stringRepository.createGameNameExample(),
                        true
                )
            }
        }
        throw RuntimeException()
    }
}