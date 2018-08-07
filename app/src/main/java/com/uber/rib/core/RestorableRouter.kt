package com.uber.rib.core

import java.io.Serializable

interface RestorableRouter {
    fun getRestorableInfo(): Serializable?
}
