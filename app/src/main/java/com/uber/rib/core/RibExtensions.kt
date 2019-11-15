@file:Suppress("UNCHECKED_CAST")

package com.uber.rib.core

import java.io.Serializable

fun <T> Bundle.getSerializable(key: String): T where T : Serializable {
    return this.wrappedBundle.getSerializable(key) as T
}

fun Bundle.toAndroidBundle(): android.os.Bundle {
    return this.wrappedBundle
}