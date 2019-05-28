@file:JvmName("MasterKey")

package com.github.niltsiar.easycrypt

import java.security.Key

class MasterKey(private val keyStorage: KeyStorage) {

    fun getOrCreate(): Key {
        if (!keyStorage.keyExists()) {
            keyStorage.generateKey()
        }
        return keyStorage.getKey()
    }
}
