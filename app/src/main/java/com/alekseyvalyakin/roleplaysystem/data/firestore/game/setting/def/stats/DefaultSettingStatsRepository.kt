package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.core.DefaultSettingFireStoreRepository
import com.google.firebase.firestore.CollectionReference


class DefaultSettingStatsRepositoryImpl :
        DefaultSettingFireStoreRepository<DefaultGameStat>(DefaultGameStat::class.java),
        DefaultSettingStatsRepository {

    override fun getCollection(): CollectionReference {
        return FirestoreCollection.DefaultStats.getDbCollection()
    }
}

interface DefaultSettingStatsRepository : FireStoreRepository<DefaultGameStat>