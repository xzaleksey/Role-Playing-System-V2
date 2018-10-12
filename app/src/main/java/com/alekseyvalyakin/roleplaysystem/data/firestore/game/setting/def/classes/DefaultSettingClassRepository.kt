package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.core.DefaultSettingFireStoreRepository
import com.google.firebase.firestore.CollectionReference

class DefaultSettingClassRepositoryImpl :
        DefaultSettingFireStoreRepository<DefaultGameClass>(DefaultGameClass::class.java),
        DefaultSettingClassRepository {

    override fun getCollection(): CollectionReference {
        return FirestoreCollection.DefaultClasses.getDbCollection()
    }
}

interface DefaultSettingClassRepository : FireStoreRepository<DefaultGameClass>