package com.alekseyvalyakin.roleplaysystem.data.firestore.game.character

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils

class FirestoreRaceHolder(
        override var id: String = StringUtils.EMPTY_STRING
) : FireStoreIdModel