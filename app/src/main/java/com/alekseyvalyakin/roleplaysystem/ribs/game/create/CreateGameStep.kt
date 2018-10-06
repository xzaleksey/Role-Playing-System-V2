package com.alekseyvalyakin.roleplaysystem.ribs.game.create

enum class CreateGameStep(val value: Int, val text: String) {
    TITLE(1, "Title") {
        override fun getPreviousStep(): CreateGameStep {
            return NONE
        }

        override fun getNextStep(): CreateGameStep {
            return DESCRIPTION
        }
    },
    DESCRIPTION(2, "Description") {
        override fun getPreviousStep(): CreateGameStep {
            return TITLE
        }

        override fun getNextStep(): CreateGameStep {
            return PASSWORD
        }
    },
    PASSWORD(3, "Password") {
        override fun getPreviousStep(): CreateGameStep {
            return DESCRIPTION
        }

        override fun getNextStep(): CreateGameStep {
            return NONE
        }
    },
    NONE(Int.MAX_VALUE, "None") {
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