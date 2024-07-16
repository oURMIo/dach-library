package com.dchistyakov.toolkit.encryption

import com.dchistyakov.toolkit.exception.BadSecretKeyException
import com.dchistyakov.toolkit.util.LanguageUtil
import com.dchistyakov.toolkit.util.RandomUtil
import java.util.Locale

/**
 * VigenereCipher implements a symmetric encryption algorithm using the Vigenere cipher technique.
 * It encrypts and decrypts text based on a repeating key, ensuring security through polyalphabetic substitution.
 *
 * @property key The key used for encryption and decryption, must not be empty.
 * @constructor Creates a VigenereCipher instance with the specified key.
 * @throws BadSecretKeyException if the provided key is empty.
 */
class VigenereCipher(private val key: String) : Cipher {

    init {
        require(key.isNotEmpty()) {
            throw BadSecretKeyException("Secret key must not be empty")
        }
    }

    /**
     * Encrypts the given plaintext using the Vigenere cipher technique.
     *
     * @param data The plaintext to be encrypted.
     * @return The encrypted ciphertext.
     */
    override fun encrypt(data: String): String {
        val normalizedData = data.replace(" ", "").uppercase(Locale.getDefault())
        val key = generateKey(normalizedData, this.key)
        val encryptedText = StringBuilder()

        for (i in normalizedData.indices) {
            val x = (normalizedData[i].code - 'A'.code + key[i].code - 'A'.code) % LanguageUtil.ENG_ALPHABET_SIZE
            val encryptedChar = (x + 'A'.code).toChar()
            encryptedText.append(encryptedChar)
        }

        return encryptedText.toString()
    }

    /**
     * Decrypts the given ciphertext using the Vigenere cipher technique.
     *
     * @param data The ciphertext to be decrypted.
     * @return The decrypted plaintext.
     */
    override fun decrypt(data: String): String {
        val key = generateKey(data, this.key)
        val decryptedText = StringBuilder()

        for (i in data.indices) {
            val x = (data[i].code - key[i].code + LanguageUtil.ENG_ALPHABET_SIZE) % LanguageUtil.ENG_ALPHABET_SIZE
            val decryptedChar = (x + 'A'.code).toChar()
            decryptedText.append(decryptedChar)
        }

        return decryptedText.toString()
    }

    /**
     * Retrieves the key used for encryption and decryption.
     *
     * @return The encryption key.
     */
    override fun getKey() = key

    /**
     * Generates a repeating key to match the length of the text.
     *
     * @param text The text to be encrypted or decrypted.
     * @param key The original key.
     * @return A repeating key that matches the length of the text.
     */
    private fun generateKey(text: String, key: String): String {
        var keyT = key.uppercase(Locale.getDefault())
        while (keyT.length < text.length) {
            keyT += keyT
        }
        return keyT.substring(0, text.length)
    }

    /**
     * Companion object providing a function to generate a random key for the VigenereCipher.
     */
    companion object {
        /**
         * Generates a random key for the VigenereCipher.
         *
         * @return A randomly generated key.
         */
        fun generateKey() = RandomUtil.generateStr()
    }
}