package com.dchistyakov.toolkit.encryption

import com.dchistyakov.toolkit.exception.BadSecretKeyException
import com.dchistyakov.toolkit.util.LanguageUtil
import com.dchistyakov.toolkit.util.RandomUtil
import java.util.Locale

/**
 * PairCipher implements a cipher algorithm using a 5x5 matrix-based technique.
 * It encrypts and decrypts text based on a given key that determines the arrangement of characters in the matrix.
 *
 * @property key The key used for encryption and decryption, determining the character arrangement in the matrix.
 * @constructor Creates a PairCipher instance with the specified key.
 * @throws BadSecretKeyException if the provided key is empty.
 */
class PairCipher(private val key: String) : Cipher {
    private val matrix: Array<CharArray> = Array(MATRIX_SIZE) { CharArray(MATRIX_SIZE) }
    private val letterPosition: MutableMap<Char, Pair<Int, Int>> = mutableMapOf()

    init {
        require(key.isNotEmpty()) {
            throw BadSecretKeyException("Secret key must not be empty")
        }

        val adjustedKey = (key.uppercase(Locale.getDefault())
            .filter { it.isLetter() && it != 'J' } + LanguageUtil.ENG_ALPHABET)
            .toCharArray()
            .distinct()
            .joinToString("")

        adjustedKey.forEachIndexed { index, char ->
            val row = index / MATRIX_SIZE
            val col = index % MATRIX_SIZE
            matrix[row][col] = char
            letterPosition[char] = Pair(row, col)
        }
    }

    /**
     * Encrypts the given plaintext using a 5x5 matrix-based pair cipher technique.
     *
     * @param data The plaintext to be encrypted.
     * @return The encrypted ciphertext.
     */
    override fun encrypt(data: String): String {
        val preparedText = prepareText(data)
        val encryptedText = StringBuilder()

        for (i in preparedText.indices step 2) {
            val char1 = preparedText[i]
            val char2 = preparedText[i + 1]
            encryptedText.append(encryptPair(char1, char2))
        }

        return encryptedText.toString()
    }

    /**
     * Decrypts the given ciphertext using a 5x5 matrix-based pair cipher technique.
     *
     * @param data The ciphertext to be decrypted.
     * @return The decrypted plaintext.
     */
    override fun decrypt(data: String): String {
        val decryptedText = StringBuilder()

        for (i in data.indices step 2) {
            val char1 = data[i]
            val char2 = data[i + 1]
            decryptedText.append(decryptPair(char1, char2))
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
     * Prepares the text for encryption by removing non-letter characters,
     * replacing 'J' with 'I', and ensuring pairs of characters for encryption.
     *
     * @param text The text to be prepared.
     * @return The prepared text suitable for encryption.
     */
    private fun prepareText(text: String): String {
        val adjustedText = text.uppercase(Locale.getDefault())
            .filter { it.isLetter() }.replace('J', 'I')
        val preparedText = StringBuilder()

        var i = 0
        while (i < adjustedText.length) {
            val char1 = adjustedText[i]
            val char2 = if (i + 1 < adjustedText.length) adjustedText[i + 1] else 'X'

            if (char1 == char2) {
                preparedText.append(char1).append('X')
                i++
            } else {
                preparedText.append(char1).append(char2)
                i += 2
            }
        }

        if (preparedText.length % 2 != 0) {
            preparedText.append('X')
        }

        return preparedText.toString()
    }

    /**
     * Encrypts a pair of characters using the 5x5 matrix-based pair cipher technique.
     *
     * @param char1 The first character of the pair.
     * @param char2 The second character of the pair.
     * @return The encrypted pair of characters.
     */
    private fun encryptPair(char1: Char, char2: Char): String {
        val (row1, col1) = letterPosition[char1]!!
        val (row2, col2) = letterPosition[char2]!!

        return when {
            row1 == row2 -> "${matrix[row1][(col1 + 1) % MATRIX_SIZE]}${matrix[row2][(col2 + 1) % MATRIX_SIZE]}"
            col1 == col2 -> "${matrix[(row1 + 1) % MATRIX_SIZE][col1]}${matrix[(row2 + 1) % MATRIX_SIZE][col2]}"
            else -> "${matrix[row1][col2]}${matrix[row2][col1]}"
        }
    }

    /**
     * Decrypts a pair of characters using the 5x5 matrix-based pair cipher technique.
     *
     * @param char1 The first character of the pair.
     * @param char2 The second character of the pair.
     * @return The decrypted pair of characters.
     */
    private fun decryptPair(char1: Char, char2: Char): String {
        val (row1, col1) = letterPosition[char1]!!
        val (row2, col2) = letterPosition[char2]!!

        return when {
            row1 == row2 ->
                buildString {
                    append(matrix[row1][(col1 + MATRIX_SIZE - 1) % MATRIX_SIZE])
                    append(matrix[row2][(col2 + MATRIX_SIZE - 1) % MATRIX_SIZE])
                }

            col1 == col2 ->
                buildString {
                    append(matrix[(row1 + MATRIX_SIZE - 1) % MATRIX_SIZE][col1])
                    append(matrix[(row2 + MATRIX_SIZE - 1) % MATRIX_SIZE][col2])
                }

            else -> "${matrix[row1][col2]}${matrix[row2][col1]}"
        }
    }

    /**
     * Companion object providing a function to generate a random key for the PairCipher.
     */
    companion object {
        /**
         * Generates a random key for the PairCipher.
         *
         * @return A randomly generated key.
         */
        fun generateKey() = RandomUtil.generateStr()
    }
}

/**
 * Constant defining the size of the matrix used by the PairCipher.
 */
private const val MATRIX_SIZE = 5
