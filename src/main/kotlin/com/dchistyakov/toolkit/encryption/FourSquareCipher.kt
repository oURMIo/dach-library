package com.dchistyakov.toolkit.encryption

import com.dchistyakov.toolkit.exception.BadSecretKeyException
import com.dchistyakov.toolkit.util.LanguageUtil
import com.dchistyakov.toolkit.util.RandomUtil
import java.util.Locale

/**
 * FourSquareCipher implements the Four-Square cipher algorithm for encrypting and decrypting text.
 *
 * The Four-Square cipher uses two 5x5 Polybius squares and an alphabet square:
 * - `square1` and `square2` are generated from the provided keys.
 * - `alphabetSquare` is a standard alphabet square excluding 'J'.
 *
 * @property firstKey the first key used to generate `square1`.
 * @property secondKey the second key used to generate `square2`.
 * @constructor Creates a FourSquareCipher instance with the specified keys.
 * @throws BadSecretKeyException if either `firstKey` or `secondKey` is empty.
 */
class FourSquareCipher(
    private val firstKey: String,
    private val secondKey: String,
) : Cipher {

    init {
        require(firstKey.isNotEmpty()) {
            throw BadSecretKeyException("First secret key must not be empty")
        }
        require(secondKey.isNotEmpty()) {
            throw BadSecretKeyException("Second secret key must not be empty")
        }
    }

    /**
     * Encrypts the given plaintext using the Four-Square cipher.
     *
     * @param data the plaintext to be encrypted.
     * @return the encrypted ciphertext.
     */
    override fun encrypt(data: String): String {
        val square1 = generateSquare(firstKey)
        val square2 = generateSquare(secondKey)
        val alphabetSquare = generateSquare("")

        val processedText = data.uppercase(Locale.getDefault())
            .replace("J", "I").filter { it in LanguageUtil.ENG_ALPHABET }
        val pairs = processedText.chunked(2).map { if (it.length == 1) it + "X" else it }

        return pairs.joinToString("") { pair ->
            val (row1, col1) = findPosition(pair[0], alphabetSquare)
            val (row2, col2) = findPosition(pair[1], alphabetSquare)
            "${square1[row1][col2]}${square2[row2][col1]}"
        }
    }

    /**
     * Decrypts the given ciphertext using the Four-Square cipher.
     *
     * @param data the ciphertext to be decrypted.
     * @return the decrypted plaintext.
     */
    override fun decrypt(data: String): String {
        val square1 = generateSquare(firstKey)
        val square2 = generateSquare(secondKey)
        val alphabetSquare = generateSquare("")

        val pairs = data.chunked(2)

        return pairs.joinToString("") { pair ->
            val (row1, col1) = findPosition(pair[0], square1)
            val (row2, col2) = findPosition(pair[1], square2)
            "${alphabetSquare[row1][col2]}${alphabetSquare[row2][col1]}"
        }
    }

    /**
     * Retrieves the keys used for encryption and decryption.
     *
     * @return a Pair containing the first and second keys.
     */
    override fun getKey() = Pair(firstKey, secondKey)

    /**
     * Encrypts the given plaintext using the Four-Square cipher with the provided keys.
     *
     * @param data the plaintext to be encrypted.
     * @param firstKey the first key used to generate `square1`.
     * @param secondKey the second key used to generate `square2`.
     * @return the encrypted ciphertext.
     */
    fun encrypt(data: String, firstKey: String, secondKey: String): String {
        val square1 = generateSquare(firstKey)
        val square2 = generateSquare(secondKey)
        val alphabetSquare = generateSquare("")

        val processedText = data.uppercase(Locale.getDefault())
            .replace("J", "I").filter { it in LanguageUtil.ENG_ALPHABET }
        val pairs = processedText.chunked(2).map { if (it.length == 1) it + "X" else it }

        return pairs.joinToString("") { pair ->
            val (row1, col1) = findPosition(pair[0], alphabetSquare)
            val (row2, col2) = findPosition(pair[1], alphabetSquare)
            "${square1[row1][col2]}${square2[row2][col1]}"
        }
    }

    /**
     * Decrypts the given ciphertext using the Four-Square cipher with the provided keys.
     *
     * @param data the ciphertext to be decrypted.
     * @param firstKey the first key used to generate `square1`.
     * @param secondKey the second key used to generate `square2`.
     * @return the decrypted plaintext.
     */
    fun decrypt(data: String, firstKey: String, secondKey: String): String {
        val square1 = generateSquare(firstKey)
        val square2 = generateSquare(secondKey)
        val alphabetSquare = generateSquare("")

        val pairs = data.chunked(2)

        return pairs.joinToString("") { pair ->
            val (row1, col1) = findPosition(pair[0], square1)
            val (row2, col2) = findPosition(pair[1], square2)
            "${alphabetSquare[row1][col2]}${alphabetSquare[row2][col1]}"
        }
    }

    /**
     * Generates a 5x5 Polybius square based on the provided key.
     *
     * @param key the key used to generate the Polybius square.
     * @return a 5x5 array of characters representing the Polybius square.
     */
    private fun generateSquare(key: String): Array<CharArray> {
        val square = Array(SQUARE_SIZE) { CharArray(SQUARE_SIZE) }
        val uniqueKey = key.uppercase(Locale.getDefault())
            .replace("J", "I").filter { it in LanguageUtil.ENG_ALPHABET }.toSet().joinToString("")
        val remainingChars = LanguageUtil.ENG_ALPHABET.filterNot { it in uniqueKey }
        val combined = (uniqueKey + remainingChars).toCharArray()

        for (i in combined.indices) {
            square[i / SQUARE_SIZE][i % SQUARE_SIZE] = combined[i]
        }

        return square
    }

    /**
     * Finds the position of a character within a given Polybius square.
     *
     * @param char the character to find within the square.
     * @param square the Polybius square to search.
     * @return a Pair containing the row and column indices of the character.
     * @throws IllegalArgumentException if the character is not found in the square.
     */
    private fun findPosition(char: Char, square: Array<CharArray>): Pair<Int, Int> {
        for (i in square.indices) {
            for (j in square[i].indices) {
                if (square[i][j] == char) return Pair(i, j)
            }
        }
        throw IllegalArgumentException("Character not found in square")
    }

    companion object {
        /**
         * Generates a random pair of keys for the Four-Square cipher.
         *
         * @return a Pair containing two randomly generated keys.
         */
        fun generateKey() = Pair(
            RandomUtil.generateStr(),
            RandomUtil.generateStr()
        )
    }
}

private const val SQUARE_SIZE: Int = 5
