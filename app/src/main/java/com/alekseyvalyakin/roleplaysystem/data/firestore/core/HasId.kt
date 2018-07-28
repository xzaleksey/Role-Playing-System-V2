package com.alekseyvalyakin.roleplaysystem.data.firestore.core

import java.io.Serializable

interface HasId : Serializable {
    var id: String
}