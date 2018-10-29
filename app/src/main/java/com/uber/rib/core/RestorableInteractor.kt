package com.uber.rib.core

import java.io.Serializable

interface RestorableInteractor {
    fun getRestorableInfo(): Serializable?
}
