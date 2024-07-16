package com.dchistyakov.toolkit.encryption

import com.dchistyakov.toolkit.exception.BadSecretKeyException
import com.dchistyakov.toolkit.util.RandomUtil

/**
 * VernamCipher implements a symmetric encryption algorithm using the Vernam cipher technique.
 * It encrypts and decrypts text based on a key of the same length as the data, ensuring strong security.
 *
 * @property key The key used for encryption and decryption, must not be empty.
 * @constructor Creates a VernamCipher instance with the specified key.
 * @throws BadSecretKeyException if the provided key is empty.
 */
class VernamCipher(private val key: String) : Cipher {

    init {
        require(key.isNotEmpty()) {
            "Secret key must not be empty"
        }
    }

    /**
     * Encrypts the given plaintext using the Vernam cipher technique.
     *
     * @param data The plaintext to be encrypted.
     * @return The encrypted ciphertext.
     * @throws IllegalArgumentException if the length of the key does not match the length of the data.
     */
    override fun encrypt(data: String): String {
        val newKey = if (data.length != key.length) {
            if (key.length < data.length) {
                placeSpace(key, data.length)
            } else {
                key.substring(0, data.length)
            }
        } else {
            key
        }

        require(data.length == newKey.length) { "The length of the key must be equal to the length of the data." }

        val cipherText = StringBuilder()
        for (i in data.indices) {
            cipherText.append((data[i].code xor newKey[i].code).toChar())
        }
        return cipherText.toString()
    }

    /**
     * Decrypts the given ciphertext using the Vernam cipher technique.
     *
     * @param data The ciphertext to be decrypted.
     * @return The decrypted plaintext.
     * @throws IllegalArgumentException if the length of the key does not match the length of the data.
     */
    override fun decrypt(data: String): String {
        val newKey = if (data.length != key.length) {
            placeSpace(key, data.length)
        } else {
            key
        }
        require(data.length == newKey.length) { "The length of the key must be equal to the length of the data." }

        val plainText = StringBuilder()
        for (i in data.indices) {
            plainText.append((data[i].code xor newKey[i].code).toChar())
        }
        return plainText.toString()
    }

    /**
     * Retrieves the key used for encryption and decryption.
     *
     * @return The encryption key.
     */
    override fun getKey() = key

    /**
     * Utility function to extend or truncate the key to match the length of the data.
     *
     * @param key The original key.
     * @param length The desired length of the key.
     * @return The adjusted key matching the specified length.
     */
    private fun placeSpace(key: String, length: Int): String {
        val newKey = StringBuilder()
        for (i in 0 until length) {
            newKey.append(key[i % key.length])
        }
        return newKey.toString()
    }

    /**
     * Companion object providing a function to generate a random key for the VernamCipher.
     */
    companion object {
        /**
         * Generates a random key for the VernamCipher.
         *
         * @return A randomly generated key.
         */
        fun generateKey() = RandomUtil.generateStr()
    }
}