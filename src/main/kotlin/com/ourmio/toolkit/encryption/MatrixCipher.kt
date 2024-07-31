package com.ourmio.toolkit.encryption

import com.ourmio.toolkit.exception.BadSecretKeyException
import com.ourmio.toolkit.util.RandomUtil

/**
 * MatrixCipher implements a cipher algorithm using a matrix transposition technique.
 * It encrypts and decrypts text based on a given key that determines the order of column rearrangement.
 *
 * @property key The key used for encryption and decryption, determining column order in the matrix.
 * @constructor Creates a MatrixCipher instance with the specified key.
 * @throws BadSecretKeyException if the provided key is empty.
 */
class MatrixCipher(private val key: String) : Cipher {

    init {
        require(key.isNotEmpty()) {
            BadSecretKeyException("Secret key must not be empty")
        }
    }

    /**
     * Encrypts the given plaintext using a matrix transposition technique.
     *
     * @param data The plaintext to be encrypted.
     * @return The encrypted ciphertext.
     */
    override fun encrypt(data: String): String {
        val keyLength = key.length
        val textLength = data.length
        val numRows = (textLength + keyLength - 1) / keyLength
        val paddedText = data.padEnd(numRows * keyLength)
        val matrix = Array(numRows) { CharArray(keyLength) }

        for (i in paddedText.indices) {
            val row = i / keyLength
            val col = i % keyLength
            matrix[row][col] = paddedText[i]
        }

        val keyIndices = key.withIndex().sortedBy { it.value }.map { it.index }
        val result = StringBuilder()

        for (col in keyIndices) {
            for (row in matrix) {
                result.append(row[col])
            }
        }

        return result.toString()
    }

    /**
     * Decrypts the given ciphertext using a matrix transposition technique.
     *
     * @param data The ciphertext to be decrypted.
     * @return The decrypted plaintext.
     */
    override fun decrypt(data: String): String {
        val keyLength = key.length
        val textLength = data.length
        val numRows = textLength / keyLength
        val matrix = Array(numRows) { CharArray(keyLength) }

        val keyIndices = key.withIndex().sortedBy { it.value }.map { it.index }
        var index = 0

        for (col in keyIndices) {
            for (i in 0 until numRows) {
                matrix[i][col] = data[index++]
            }
        }

        val result = StringBuilder()
        for (row in matrix) {
            result.append(row)
        }

        return result.toString().trimEnd()
    }

    /**
     * Retrieves the key used for encryption and decryption.
     *
     * @return The encryption key.
     */
    override fun getKey() = key

    /**
     * Encrypts the given plaintext using a matrix transposition technique with the provided key.
     *
     * @param data The plaintext to be encrypted.
     * @param key The key used for encryption, determining column order in the matrix.
     * @return The encrypted ciphertext.
     */
    fun encrypt(data: String, key: String): String {
        val keyLength = key.length
        val textLength = data.length
        val numRows = (textLength + keyLength - 1) / keyLength
        val paddedText = data.padEnd(numRows * keyLength)
        val matrix = Array(numRows) { CharArray(keyLength) }

        for (i in paddedText.indices) {
            val row = i / keyLength
            val col = i % keyLength
            matrix[row][col] = paddedText[i]
        }

        val keyIndices = key.withIndex().sortedBy { it.value }.map { it.index }
        val result = StringBuilder()

        for (col in keyIndices) {
            for (row in matrix) {
                result.append(row[col])
            }
        }

        return result.toString()
    }

    /**
     * Decrypts the given ciphertext using a matrix transposition technique with the provided key.
     *
     * @param data The ciphertext to be decrypted.
     * @param key The key used for decryption, determining column order in the matrix.
     * @return The decrypted plaintext.
     */
    fun decrypt(data: String, key: String): String {
        val keyLength = key.length
        val textLength = data.length
        val numRows = textLength / keyLength
        val matrix = Array(numRows) { CharArray(keyLength) }

        val keyIndices = key.withIndex().sortedBy { it.value }.map { it.index }
        var index = 0

        for (col in keyIndices) {
            for (i in 0 until numRows) {
                matrix[i][col] = data[index++]
            }
        }

        val result = StringBuilder()
        for (row in matrix) {
            result.append(row)
        }

        return result.toString().trimEnd()
    }

    companion object {
        /**
         * Generates a random key for the MatrixCipher.
         *
         * @return A randomly generated key.
         */
        fun generateKey() = RandomUtil.generateStr()
    }
}