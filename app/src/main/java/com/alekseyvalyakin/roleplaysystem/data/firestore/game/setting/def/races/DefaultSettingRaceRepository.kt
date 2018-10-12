package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.core.DefaultSettingFireStoreRepository
import com.google.firebase.firestore.CollectionReference

class DefaultSettingRaceRepositoryImpl :
        DefaultSettingFireStoreRepository<DefaultGameRace>(DefaultGameRace::class.java),
        DefaultSettingRaceRepository {

    override fun getCollection(): CollectionReference {
        return FirestoreCollection.DefaultRaces.getDbCollection()
    }
}

interface DefaultSettingRaceRepository : FireStoreRepository<DefaultGameRace>