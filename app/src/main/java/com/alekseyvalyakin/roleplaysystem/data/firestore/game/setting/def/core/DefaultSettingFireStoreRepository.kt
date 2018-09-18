package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.core

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasId
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.BaseGameFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.GameSetting
import com.google.firebase.firestore.DocumentReference
import io.reactivex.Single

abstract class DefaultSettingFireStoreRepository<T : HasId>(
        clazz: Class<T>
) : BaseGameFireStoreRepository<T>(clazz) {

    override fun createDocument(gameId: String, data: T): Single<T> {
        throw UnsupportedOperationException()
    }

    protected fun getDefaultSettingDocument(): DocumentReference {
        return FirestoreCollection.SETTINGS.getDbCollection().document(GameSetting.DEFAULT.title)
    }
}