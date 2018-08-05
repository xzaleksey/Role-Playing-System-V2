package com.alekseyvalyakin.roleplaysystem.ribs.game.create

enum class CreateGameStep(val value: Int) {
    TITLE(1) {
        override fun getPreviousStep(): CreateGameStep {
            return NONE
        }

        override fun getNextStep(): CreateGameStep {
            return DESCRIPTION
        }
    },
    DESCRIPTION(2) {
        override fun getPreviousStep(): CreateGameStep {
            return TITLE
        }

        override fun getNextStep(): CreateGameStep {
            return PASSWORD
        }
    },
    PASSWORD(3) {
        override fun getPreviousStep(): CreateGameStep {
            return DESCRIPTION
        }

        override fun getNextStep(): CreateGameStep {
            return NONE
        }
    },
    NONE(Int.MAX_VALUE) {
        override fun getPreviousStep(): CreateGameStep {
            return NONE
        }

        override fun getNextStep(): CreateGameStep {
            return NONE
        }
    };

    abstract fun getPreviousStep(): CreateGameStep

    abstract fun getNextStep(): CreateGameStep
}