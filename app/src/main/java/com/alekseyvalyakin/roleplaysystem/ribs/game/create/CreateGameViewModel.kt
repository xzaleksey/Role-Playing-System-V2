package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import android.os.Parcel
import android.os.Parcelable

data class CreateGameViewModel(
        val title: String,
        val stepText: String,
        val step: CreateGameStep,
        val inputText: String,
        val inputMaxLines: Int,
        val inputHint: String,
        val inputExample: String,
        val required: Boolean
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readSerializable() as CreateGameStep,
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(stepText)
        parcel.writeSerializable(step)
        parcel.writeString(inputText)
        parcel.writeInt(inputMaxLines)
        parcel.writeString(inputHint)
        parcel.writeString(inputExample)
        parcel.writeByte(if (required) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CreateGameViewModel> {
        const val KEY = "KEY"

        override fun createFromParcel(parcel: Parcel): CreateGameViewModel {
            return CreateGameViewModel(parcel)
        }

        override fun newArray(size: Int): Array<CreateGameViewModel?> {
            return arrayOfNulls(size)
        }
    }
}