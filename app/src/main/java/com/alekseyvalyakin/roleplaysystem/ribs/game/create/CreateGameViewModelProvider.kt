package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils

class CreateGameViewModelProvider constructor(
        private val stringRepository: StringRepository
) {
    private val maxSteps = 3

    fun getCreateGameViewModel(step: CreateGameStep): CreateGameViewModel {
        when (step) {
            CreateGameStep.TITLE -> {
                return CreateGameViewModel(
                        stringRepository.createGameNameTitle(),
                        stringRepository.createGameStepText(CreateGameStep.TITLE.value, maxSteps),
                        CreateGameStep.TITLE,
                        StringUtils.EMPTY_STRING,
                        stringRepository.name(),
                        stringRepository.createGameNameExample(),
                        true
                )
            }
        }
        throw RuntimeException()
    }
}