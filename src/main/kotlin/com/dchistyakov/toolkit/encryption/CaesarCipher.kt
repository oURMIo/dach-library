package com.dchistyakov.toolkit.encryption

import com.dchistyakov.toolkit.exception.BadSecretKeyException
import com.dchistyakov.toolkit.util.LanguageUtil
import com.dchistyakov.toolkit.util.RandomUtil

/**
 * CaesarCipher implements a simple substitution cipher where each letter in the plaintext
 * is shifted a certain number of places down or up the alphabet.
 *
 * @property key the shift value used for encryption and decryption. Must be positive.
 * @constructor Creates a CaesarCipher instance with the specified key.
 * @throws BadSecretKeyException if the provided key is not positive.
 */
class CaesarCipher(private val key: Int) : Cipher {

    init {
        require(key > 0) { throw BadSecretKeyException("Key must be a positive integer") }
    }

    /**
     * Encrypts the given plaintext using the Caesar cipher with the specified key.
     *
     * @param data the plaintext to be encrypted.
     * @return the encrypted ciphertext.
     */
    override fun encrypt(data: String): String = caesarCipherEncrypt(data, key)

    /**
     * Decrypts the given ciphertext using the Caesar cipher with the specified key.
     *
     * @param data the ciphertext to be decrypted.
     * @return the decrypted plaintext.
     */
    override fun decrypt(data: String): String =
        caesarCipherEncrypt(data, LanguageUtil.ENG_ALPHABET_SIZE - (key % LanguageUtil.ENG_ALPHABET_SIZE))

    /**
     * Returns the key used for encryption and decryption.
     *
     * @return the key value.
     */
    override fun getKey() = key

    /**
     * Performs the Caesar cipher encryption or decryption on the given text based on the shift value.
     *
     * @param text the text to be encrypted or decrypted.
     * @param shift the shift value for the Caesar cipher.
     * @return the encrypted or decrypted text.
     */
    private fun caesarCipherEncrypt(text: String, shift: Int): String {
        val result = StringBuilder()

        for (char in text) {
            if (char.isLetter()) {
                val base = if (char.isUpperCase()) 'A' else 'a'
                val shiftedChar =
                    ((char - base + shift) % LanguageUtil.ENG_ALPHABET_SIZE + base.code).toChar()
                result.append(shiftedChar)
            } else {
                result.append(char)
            }
        }
        return result.toString()
    }

    companion object {
        /**
         * Generates a random key for the Caesar cipher.
         *
         * @return a randomly generated positive integer key.
         */
        fun generateKey() = RandomUtil.generateInt()
    }
}