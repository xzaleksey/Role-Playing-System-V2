package com.alekseyvalyakin.roleplaysystem.data.character

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.character.FirestoreItemHolder
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.item.FireStoreItem

data class CharacterItem(
        val gameItem: FireStoreItem,
        val itemHolder: FirestoreItemHolder
)