package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.core.DefaultSettingFireStoreRepository
import com.google.firebase.firestore.CollectionReference

class DefaultSettingSkillsRepositoryImpl : DefaultSettingFireStoreRepository<DefaultGameSkill>(DefaultGameSkill::class.java),
        DefaultSettingSkillsRepository {

    override fun getCollection(): CollectionReference {
        return FirestoreCollection.DefaultSkills.getDbCollection()
    }
}

interface DefaultSettingSkillsRepository : FireStoreRepository<DefaultGameSkill>