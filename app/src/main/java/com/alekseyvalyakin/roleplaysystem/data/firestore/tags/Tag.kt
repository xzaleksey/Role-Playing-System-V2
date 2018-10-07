package com.alekseyvalyakin.roleplaysystem.data.firestore.tags

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils

data class Tag(
        override var id: String = StringUtils.EMPTY_STRING
) : FireStoreIdModel