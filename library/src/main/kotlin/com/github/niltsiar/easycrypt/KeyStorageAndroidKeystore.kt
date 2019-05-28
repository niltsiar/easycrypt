@file:JvmName("KeyStorageAndroidKeyStore")

package com.github.niltsiar.easycrypt

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.Key
import java.security.KeyStore
import javax.crypto.KeyGenerator

@TargetApi(Build.VERSION_CODES.M)
private const val ENCRYPTION_BLOCK_MODE: String = KeyProperties.BLOCK_MODE_CBC
@TargetApi(Build.VERSION_CODES.M)
private const val ENCRYPTION_ALGORITHM: String = KeyProperties.KEY_ALGORITHM_AES
@TargetApi(Build.VERSION_CODES.M)
private const val ENCRYPTION_PADDING: String = KeyProperties.ENCRYPTION_PADDING_PKCS7
private const val KEY_SIZE: Int = 256

@TargetApi(Build.VERSION_CODES.M)
class KeyStorageAndroidKeystore : KeyStorage {

    override fun keyExists(): Boolean {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        return keyStore.containsAlias(MASTER_KEY_ALIAS)
    }

    override fun generateKey() {
        val keyGenerator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM, ANDROID_KEYSTORE)
        keyGenerator.init(createAES256GCMKeyGenParameterSpec(MASTER_KEY_ALIAS))
        keyGenerator.generateKey()
    }

    override fun getKey(): Key {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        return keyStore.getKey(MASTER_KEY_ALIAS, null)
    }

}

@TargetApi(Build.VERSION_CODES.M)
internal fun createAES256GCMKeyGenParameterSpec(keyAlias: String): KeyGenParameterSpec {
    val purpose = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    val builder = KeyGenParameterSpec.Builder(keyAlias, purpose)
        .setBlockModes(ENCRYPTION_BLOCK_MODE)
        .setEncryptionPaddings(ENCRYPTION_PADDING)
        .setKeySize(KEY_SIZE)

    return builder.build()
}
