package com.alekseyvalyakin.roleplaysystem.data.firestore.game.log

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDateCreate
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.BaseGameFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.GameFireStoreRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import io.reactivex.Flowable


class LogRepositoryImpl : BaseGameFireStoreRepository<LogMessage>(LogMessage::class.java), LogRepository {

    override fun observeLogMessagesOrdered(gameId: String): Flowable<List<LogMessage>> {
        val query = getCollection(gameId)
                .orderBy(HasDateCreate.FIELD_DATE_CREATE, Query.Direction.DESCENDING)
        return observeQueryCollection(query, gameId)
    }

    override fun getCollection(gameId: String): CollectionReference {
        return FirestoreCollection.LogsInGame(gameId).getDbCollection()
    }

}

interface LogRepository : GameFireStoreRepository<LogMessage> {
    fun observeLogMessagesOrdered(gameId: String): Flowable<List<LogMessage>>
}