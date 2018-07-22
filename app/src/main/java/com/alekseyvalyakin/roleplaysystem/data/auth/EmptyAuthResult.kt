package com.alekseyvalyakin.roleplaysystem.data.auth

import android.annotation.SuppressLint
import android.os.Parcel
import com.google.firebase.auth.AdditionalUserInfo
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

@SuppressLint("ParcelCreator")
object EmptyAuthResult : AuthResult {
    override fun getAdditionalUserInfo(): AdditionalUserInfo? {
        return null
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {

    }

    override fun getUser(): FirebaseUser? {
        return null
    }

    override fun describeContents(): Int {
        return 0
    }

}

