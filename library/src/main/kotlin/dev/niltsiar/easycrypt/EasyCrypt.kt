@file:JvmName("EasyCryptUtils")

package dev.niltsiar.easycrypt

import android.os.Build
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

class EasyCrypt(private val keyStorage: KeyStorage = getKeyStorage()) {

    companion object {

        @JvmStatic
        val instance: EasyCrypt by lazy { EasyCrypt() }
    }

    fun encrypt(value: String): String {
        val cipher = Cipher.getInstance(keyStorage.encryptionTransformation)
        cipher.init(Cipher.ENCRYPT_MODE, keyStorage.getKey())
        val iv = cipher.iv
        val encryptedValue = cipher.doFinal(value.toByteArray())

        return "${iv.toBase64()}$IV_SEPARATOR${encryptedValue.toBase64()}"
    }

    fun decrypt(value: String): String {
        val (iv, data) = value.split(IV_SEPARATOR)
        val ivParameterSpec = IvParameterSpec(iv.fromBase64())
        val cipher = Cipher.getInstance(keyStorage.encryptionTransformation)
        cipher.init(Cipher.DECRYPT_MODE, keyStorage.getKey(), ivParameterSpec)

        val decryptedValue = cipher.doFinal(data.fromBase64())
        return String(decryptedValue)
    }
}

@JvmSynthetic
@JvmField
internal val isMarshmallowOrGreater: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

@JvmSynthetic
internal fun getKeyStorage(): KeyStorage {
    return if (isMarshmallowOrGreater) {
        KeyStorageAndroidKeystore()
    } else {
        KeyStorageSharedPreferences(EasyCryptInitProvider.applicationContext)
    }
}

internal fun ByteArray.toBase64(): String = Base64.encodeToString(this, Base64.NO_WRAP)
internal fun String.fromBase64(): ByteArray = Base64.decode(this, Base64.NO_WRAP)
