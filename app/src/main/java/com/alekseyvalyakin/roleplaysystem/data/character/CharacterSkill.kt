package com.alekseyvalyakin.roleplaysystem.data.character

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.character.FirestoreSkillHolder
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency.DependencyInfo
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.UserGameSkill
import com.alekseyvalyakin.roleplaysystem.data.formula.FormulaEvaluator

data class CharacterSkill(
        val userGameSkill: UserGameSkill,
        val skillHolder: FirestoreSkillHolder,
        val dependencies: MutableList<DependencyInfo>,
        val successFormulaParser: FormulaEvaluator,
        val resultFormulaParser: FormulaEvaluator? = null
)