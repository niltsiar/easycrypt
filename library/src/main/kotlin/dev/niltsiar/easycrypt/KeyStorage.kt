@file:JvmName("KeyStorageUtils")

package dev.niltsiar.easycrypt

import java.security.Key
import java.security.KeyStore

@JvmSynthetic
internal const val ANDROID_KEYSTORE: String = "AndroidKeyStore"
@JvmSynthetic
internal const val MASTER_KEY_ALIAS: String = "easy_crypt_master_key"
@JvmSynthetic
internal const val IV_SEPARATOR: String = "@"

interface KeyStorage {
    fun keyExists(): Boolean
    fun generateKey()
    fun getKey(): Key
    val encryptionTransformation: String
}

@JvmSynthetic
internal fun getKeyStore(): KeyStore {
    val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
    keyStore.load(null)
    return keyStore
}
