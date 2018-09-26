package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasName
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.BaseGameFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.GameFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.UserGameStat
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import io.reactivex.Completable
import io.reactivex.Flowable


class GameClassRepositoryImpl : BaseGameFireStoreRepository<UserGameClass>(UserGameClass::class.java), GameClassRepository {

    override fun observeDiceCollectionsOrdered(gameId: String): Flowable<List<UserGameClass>> {
        val query = getCollection(gameId)
                .orderBy(HasName.NAME_FIELD, Query.Direction.ASCENDING)
        return observeQueryCollection(query, gameId)
    }

    override fun getCollection(gameId: String): CollectionReference {
        return FirestoreCollection.ClassesInGame(gameId).getDbCollection()
    }

    override fun setSelected(gameId: String, id: String, selected: Boolean): Completable {
        return updateFieldOffline(id, selected, UserGameStat.SELECTED_FIELD, gameId)
    }

}

interface GameClassRepository : GameFireStoreRepository<UserGameClass> {
    fun observeDiceCollectionsOrdered(gameId: String): Flowable<List<UserGameClass>>

    fun setSelected(gameId: String, id: String, selected: Boolean): Completable
}