package com.alekseyvalyakin.roleplaysystem.crypto

class SimpleCryptoProviderImpl(
        private val key: String
) : SimpleCryptoProvider {

    override fun getSimpleCrypto(salt: String): SimpleCrypto {
        return SimpleCrypto.getDefault(key, salt, ByteArray(16))
    }
}

interface SimpleCryptoProvider {
    fun getSimpleCrypto(salt: String): SimpleCrypto
}