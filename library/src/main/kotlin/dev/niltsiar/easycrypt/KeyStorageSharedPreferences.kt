@file:JvmName("KeyStorageSharedPreferencesUtils")

package dev.niltsiar.easycrypt

import android.content.Context
import android.content.SharedPreferences
import android.security.KeyPairGeneratorSpec
import java.math.BigInteger
import java.security.Key
import java.security.KeyPairGenerator
import java.security.spec.AlgorithmParameterSpec
import java.util.Calendar
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.security.auth.x500.X500Principal

@JvmSynthetic
internal const val RSA_ALGORITHM: String = "RSA"
@JvmSynthetic
internal const val RSA_BLOCK_MODE: String = "ECB"
@JvmSynthetic
internal const val RSA_PADDING: String = "PKCS1Padding"
@JvmSynthetic
internal const val RSA_KEY_SIZE: Int = 2048
@JvmSynthetic
internal const val RSA_TRANSFORMATION: String = "$RSA_ALGORITHM/$RSA_BLOCK_MODE/$RSA_PADDING"
@JvmSynthetic
internal const val AES_ALGORITHM: String = "AES"
@JvmSynthetic
internal const val AES_BLOCK_MODE: String = "CBC"
@JvmSynthetic
internal const val AES_PADDING: String = "PKCS7Padding"
@JvmSynthetic
internal const val AES_KEY_SIZE: Int = 256
@JvmSynthetic
internal const val ENCRYPTION_SHARED_PREFERENCES_NAME: String = "EasyCryptPreferences"

class KeyStorageSharedPreferences(
    private val context: Context,
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(ENCRYPTION_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
) : KeyStorage {

    init {
        if (!keyExists()) {
            generateKey()
        }
    }

    override val encryptionTransformation: String = "$AES_ALGORITHM/$AES_BLOCK_MODE/$AES_PADDING"

    override fun keyExists(): Boolean = getKeyStore().containsAlias(MASTER_KEY_ALIAS) && sharedPreferences.contains(MASTER_KEY_ALIAS)

    override fun generateKey() {
        val keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM, ANDROID_KEYSTORE)
        keyPairGenerator.initialize(createKeyPairGenParameterSpec(context, MASTER_KEY_ALIAS))
        val keyPair = keyPairGenerator.generateKeyPair()

        val keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM)
        keyGenerator.init(AES_KEY_SIZE)
        val secretKey = keyGenerator.generateKey()

        val cipher = Cipher.getInstance(RSA_TRANSFORMATION)
        cipher.init(Cipher.WRAP_MODE, keyPair.public)
        val encrypytedKey = cipher.wrap(secretKey).toBase64()

        sharedPreferences.edit().putString(MASTER_KEY_ALIAS, encrypytedKey).commit()
    }

    override fun getKey(): Key {
        val encryptedKey = sharedPreferences.getString(MASTER_KEY_ALIAS, null)
        checkNotNull(encryptedKey) { "StoredKey should not be null" }

        val keyStore = getKeyStore()
        val privateKey = keyStore.getKey(MASTER_KEY_ALIAS, null)

        checkNotNull(privateKey) { "Certificate to decrypt secret key should not be null" }

        val cipher = Cipher.getInstance(RSA_TRANSFORMATION)
        cipher.init(Cipher.UNWRAP_MODE, privateKey)

        return cipher.unwrap(encryptedKey.fromBase64(), AES_ALGORITHM, Cipher.SECRET_KEY)
    }
}

@JvmSynthetic
internal fun createKeyPairGenParameterSpec(context: Context, keyAlias: String): AlgorithmParameterSpec {
    val startDate = Calendar.getInstance()
    val endDate = Calendar.getInstance()
    endDate.add(Calendar.YEAR, 100)

    val builder = KeyPairGeneratorSpec.Builder(context)
        .setAlias(keyAlias)
        .setKeySize(RSA_KEY_SIZE)
        .setSerialNumber(BigInteger.ONE)
        .setSubject(X500Principal("CN=$keyAlias CA Certificate"))
        .setStartDate(startDate.time)
        .setEndDate(endDate.time)

    return builder.build()
}
