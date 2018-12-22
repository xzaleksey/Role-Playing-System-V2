package com.alekseyvalyakin.roleplaysystem.data.firestore.user.currentUser

import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.auth.FirebaseUser

class CurrentUserInfo constructor(
        firebaseUser: FirebaseUser,
        val uid: String = firebaseUser.uid,
        val name: String = firebaseUser.displayName ?: StringUtils.EMPTY_STRING,
        val providerId: String = firebaseUser.providerId,
        val isAnonymous: Boolean = firebaseUser.isAnonymous,
        val email: String? = firebaseUser.email,
        val photoUrl: String = firebaseUser.photoUrl.toString()
)