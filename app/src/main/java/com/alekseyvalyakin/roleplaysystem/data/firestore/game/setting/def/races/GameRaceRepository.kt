package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasName
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.Selectable
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.BaseGameFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.GameFireStoreRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import io.reactivex.Completable
import io.reactivex.Flowable


class GameRaceRepositoryImpl : BaseGameFireStoreRepository<UserGameRace>(UserGameRace::class.java), GameRaceRepository {

    override fun observeCollectionsOrdered(gameId: String): Flowable<List<UserGameRace>> {
        val query = getCollection(gameId)
                .orderBy(HasName.NAME_FIELD, Query.Direction.ASCENDING)
        return observeQueryCollection(query, gameId)
    }

    override fun getCollection(gameId: String): CollectionReference {
        return FirestoreCollection.RacesInGame(gameId).getDbCollection()
    }

    override fun setSelected(gameId: String, id: String, selected: Boolean): Completable {
        return updateFieldOffline(id, selected, Selectable.SELECTED_FIELD, gameId)
    }

}

interface GameRaceRepository : GameFireStoreRepository<UserGameRace> {
    fun observeCollectionsOrdered(gameId: String): Flowable<List<UserGameRace>>

    fun setSelected(gameId: String, id: String, selected: Boolean): Completable
}