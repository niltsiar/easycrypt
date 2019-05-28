@file:JvmName("KeyStorage")

package com.github.niltsiar.easycrypt

import java.security.Key
import java.security.KeyStore

interface KeyStorage {
    fun keyExists(): Boolean
    fun generateKey()
    fun getKey(): Key
}

internal const val ANDROID_KEYSTORE: String = "AndroidKeyStore"
internal const val MASTER_KEY_ALIAS: String = "easy_crypt_master_key"

internal fun getKeyStore(): KeyStore {
    val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
    keyStore.load(null)
    return keyStore
}
