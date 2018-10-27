package com.alekseyvalyakin.roleplaysystem.data.firestore.game.character

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasCount
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils

class FirestoreItemHolder(
        override var id: String = StringUtils.EMPTY_STRING,
        override var count: Int = 1
) : FireStoreIdModel, HasCount