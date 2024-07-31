package com.ourmio.test.encryption

import com.ourmio.toolkit.encryption.PairCipher
import com.ourmio.toolkit.util.RandomUtil
import java.util.Locale
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class PairCipherTest {

    @ParameterizedTest(name = "Test encryption and decryption with key size {1}")
    @CsvSource(
        "Welcome to my world, 10",
        "Welcome to my world, 21",
        "Welcome to my world, 40"
    )
    fun testEncryptionDecryptionWithVariousKeySizes(text: String, keySize: Int) {
        val securityKey = RandomUtil.generateStr(keySize)
        val pairCipher = PairCipher(key = securityKey)
        assertEquals(securityKey, pairCipher.getKey())

        val encryptText = pairCipher.encrypt(data = text)
        assertNotEquals(text, encryptText, "Encrypted text should not match the original text")

        val decryptText = pairCipher.decrypt(data = encryptText)
        assertNotEquals(encryptText, decryptText, "Decrypted text should not match the encrypted text")

        val normalizedText = text.replace(" ", "").uppercase(Locale.getDefault())
        assertEquals(normalizedText, decryptText, "Decrypted normalized text should match the original text")
    }

    @Test
    fun testEmptyStringEncryptionDecryption() {
        val text = ""
        val securityKey = PairCipher.generateKey()
        val pairCipher = PairCipher(key = securityKey)
        assertEquals(securityKey, pairCipher.getKey())

        val encryptText = pairCipher.encrypt(data = text)
        assertEquals(text, encryptText, "Encrypted empty text should be empty")

        val decryptText = pairCipher.decrypt(data = encryptText)
        assertEquals(text, decryptText, "Decrypted empty text should be empty")
    }

    @Test
    fun testLargeKeyEncryptionDecryption() {
        val text = "Welcome to my world"
        val securityKey = RandomUtil.generateStr(text.length * 10)
        val pairCipher = PairCipher(key = securityKey)
        assertEquals(securityKey, pairCipher.getKey())

        val encryptText = pairCipher.encrypt(data = text)
        assertNotEquals(text, encryptText, "Encrypted text should not match the original text")

        val decryptText = pairCipher.decrypt(data = encryptText)
        assertNotEquals(encryptText, decryptText, "Decrypted text should not match the encrypted text")

        val normalizedText = text.replace(" ", "").uppercase(Locale.getDefault())
        assertEquals(normalizedText, decryptText, "Decrypted normalized text should match the original text")
    }
}