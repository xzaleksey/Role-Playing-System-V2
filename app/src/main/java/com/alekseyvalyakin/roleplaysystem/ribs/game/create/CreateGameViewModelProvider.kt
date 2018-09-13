package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import android.text.InputType
import com.alekseyvalyakin.roleplaysystem.crypto.SimpleCryptoProvider
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils

class CreateGameViewModelProvider constructor(
        private val stringRepository: StringRepository,
        private val simpleCryptoProvider: SimpleCryptoProvider
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
                        true,
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                )
            }
            CreateGameStep.DESCRIPTION -> {
                return CreateGameViewModel(
                        stringRepository.createGameDescriptionTitle(),
                        stringRepository.createGameStepText(CreateGameStep.DESCRIPTION.value, maxSteps),
                        CreateGameStep.DESCRIPTION,
                        game.description,
                        Int.MAX_VALUE,
                        stringRepository.description(),
                        stringRepository.createGameDescriptionExample(),
                        true,
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                )
            }
            CreateGameStep.PASSWORD -> {
                val inputText = if (game.password.isEmpty()) {
                    StringUtils.EMPTY_STRING
                } else {
                    simpleCryptoProvider.getSimpleCrypto(game.masterId).decrypt(game.password)
                }

                return CreateGameViewModel(
                        stringRepository.createGamePasswordTitle(),
                        stringRepository.createGameStepText(CreateGameStep.PASSWORD.value, maxSteps),
                        CreateGameStep.PASSWORD,
                        inputText,
                        Int.MAX_VALUE,
                        stringRepository.getInputPassword(),
                        StringUtils.EMPTY_STRING,
                        false,
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                )
            }
            else -> throw UnsupportedOperationException("Unsupported step")
        }
    }

    fun getCreateGameViewModel(game: Game): CreateGameViewModel {
        return when {
            game.name.isBlank() -> getCreateGameViewModel(CreateGameStep.TITLE, game)
            game.description.isBlank() -> getCreateGameViewModel(CreateGameStep.DESCRIPTION, game)
            else -> getCreateGameViewModel(CreateGameStep.PASSWORD, game)
        }
    }

}