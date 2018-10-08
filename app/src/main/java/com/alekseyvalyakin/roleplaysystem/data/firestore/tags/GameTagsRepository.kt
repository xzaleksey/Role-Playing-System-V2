package com.alekseyvalyakin.roleplaysystem.data.firestore.tags

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDateCreate
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.BaseGameFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.GameFireStoreRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import io.reactivex.Flowable

class GameTagsRepositoryImpl() : BaseGameFireStoreRepository<Tag>(Tag::class.java), GameTagsRepository {

    override fun observeTagsOrdered(gameId: String): Flowable<List<Tag>> {
        val query = getCollection(gameId)
                .orderBy(HasDateCreate.FIELD_DATE_CREATE, Query.Direction.DESCENDING)
        return observeQueryCollection(query, gameId)
    }


    override fun getCollection(gameId: String): CollectionReference {
        return FirestoreCollection.TagsInGame(gameId).getDbCollection()
    }

}

interface GameTagsRepository : GameFireStoreRepository<Tag> {
    fun observeTagsOrdered(gameId: String): Flowable<List<Tag>>

//    fun addSkill(id: String, skillId: String): Completable
}