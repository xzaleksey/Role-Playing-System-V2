package com.alekseyvalyakin.roleplaysystem.utils.keyboard

import io.reactivex.Observable

interface KeyboardStateProvider {
    fun observeKeyboardState(): Observable<Boolean>
}
