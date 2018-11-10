package com.alekseyvalyakin.roleplaysystem.data.sound

interface Encoder {
    fun encode(buf: ShortArray)

    fun close()
}
