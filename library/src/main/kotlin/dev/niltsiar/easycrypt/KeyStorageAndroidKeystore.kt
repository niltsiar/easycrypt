@file:JvmName("KeyStorageAndroidKeyStoreUtils")

package dev.niltsiar.easycrypt

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.Key
import javax.crypto.KeyGenerator

@TargetApi(Build.VERSION_CODES.M)
@JvmSynthetic
internal const val ANDROID_KEYSTORE_ENCRYPTION_ALGORITHM: String = KeyProperties.KEY_ALGORITHM_AES
@TargetApi(Build.VERSION_CODES.M)
@JvmSynthetic
internal const val ANDROID_KEYSTORE_ENCRYPTION_BLOCK_MODE: String = KeyProperties.BLOCK_MODE_CBC
@TargetApi(Build.VERSION_CODES.M)
@JvmSynthetic
internal const val ANDROID_KEYSTORE_ENCRYPTION_PADDING: String = KeyProperties.ENCRYPTION_PADDING_PKCS7
@JvmSynthetic
internal const val ANDROID_KEYSTORE_KEY_SIZE: Int = 256

@TargetApi(Build.VERSION_CODES.M)
class KeyStorageAndroidKeystore : KeyStorage {

    init {
        if (!keyExists()) {
            generateKey()
        }
    }

    override val encryptionTransformation: String =
        "$ANDROID_KEYSTORE_ENCRYPTION_ALGORITHM/$ANDROID_KEYSTORE_ENCRYPTION_BLOCK_MODE/$ANDROID_KEYSTORE_ENCRYPTION_PADDING"

    override fun keyExists(): Boolean = getKeyStore().containsAlias(MASTER_KEY_ALIAS)

    override fun generateKey() {
        val keyGenerator = KeyGenerator.getInstance(ANDROID_KEYSTORE_ENCRYPTION_ALGORITHM, ANDROID_KEYSTORE)
        keyGenerator.init(createKeyGenParameterSpec(MASTER_KEY_ALIAS))
        keyGenerator.generateKey()
    }

    override fun getKey(): Key {
        val key = getKeyStore().getKey(MASTER_KEY_ALIAS, null)

        checkNotNull(key) { "StoredKey should not be null" }

        return key
    }
}

@TargetApi(Build.VERSION_CODES.M)
@JvmSynthetic
internal fun createKeyGenParameterSpec(keyAlias: String): KeyGenParameterSpec {
    val purpose = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    val builder = KeyGenParameterSpec.Builder(keyAlias, purpose)
        .setBlockModes(ANDROID_KEYSTORE_ENCRYPTION_BLOCK_MODE)
        .setEncryptionPaddings(ANDROID_KEYSTORE_ENCRYPTION_PADDING)
        .setKeySize(ANDROID_KEYSTORE_KEY_SIZE)

    return builder.build()
}
