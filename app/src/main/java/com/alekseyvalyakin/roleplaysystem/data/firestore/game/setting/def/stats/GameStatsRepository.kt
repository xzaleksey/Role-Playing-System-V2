package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasName
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.BaseGameFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.GameFireStoreRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import io.reactivex.Flowable


class GameStatsRepositoryImpl : BaseGameFireStoreRepository<UserGameStat>(UserGameStat::class.java), GameStatsRepository {

    override fun observeDiceCollectionsOrdered(gameId: String): Flowable<List<UserGameStat>> {
        val query = getCollection(gameId)
                .orderBy(HasName.NAME_FIELD, Query.Direction.ASCENDING)
        return observeQueryCollection(query, gameId)
    }

    override fun getCollection(gameId: String): CollectionReference {
        return FirestoreCollection.StatsInGame(gameId).getDbCollection()
    }

}

interface GameStatsRepository : GameFireStoreRepository<UserGameStat> {
    fun observeDiceCollectionsOrdered(gameId: String): Flowable<List<UserGameStat>>
}