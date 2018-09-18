package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.GameFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.core.DefaultSettingFireStoreRepository
import com.google.firebase.firestore.CollectionReference


class DefaultSettingStatsRepositoryImpl :
        DefaultSettingFireStoreRepository<DefaultGameStat>(DefaultGameStat::class.java),
        DefaultSettingStatsRepository {

    override fun getCollection(gameId: String): CollectionReference {
        return FirestoreCollection.DEFAULT_STATS.getDbCollection()
    }
}

interface DefaultSettingStatsRepository : GameFireStoreRepository<DefaultGameStat>