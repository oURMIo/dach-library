package com.ourmio.test.encryption

import com.ourmio.toolkit.encryption.VigenereCipher
import com.ourmio.toolkit.util.RandomUtil
import java.util.Locale
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class VigenereCipherTest {

    @ParameterizedTest(name = "Test encryption and decryption with key size {1}")
    @CsvSource(
        "Welcome to my world, 10",
        "Welcome to my world, 21",
        "Welcome to my world, 40"
    )
    fun testEncryptionDecryptionWithVariousKeySizes(text: String, keySize: Int) {
        val securityKey = RandomUtil.generateStr(size = keySize)
        val vigenereCipher = VigenereCipher(key = securityKey)
        assertEquals(securityKey, vigenereCipher.getKey())

        val encryptText = vigenereCipher.encrypt(data = text)
        assertNotEquals(text, encryptText, "Encrypted text should not match the original text")

        val decryptText = vigenereCipher.decrypt(data = encryptText)
        assertNotEquals(encryptText, decryptText, "Decrypted text should not match the encrypted text")

        val normalizedText = text.replace(" ", "").uppercase(Locale.getDefault())
        assertEquals(normalizedText, decryptText, "Decrypted normalized text should match the original text")
    }

    @Test
    fun testEmptyStringEncryptionDecryption() {
        val text = ""
        val securityKey = VigenereCipher.generateKey()
        val vigenereCipher = VigenereCipher(key = securityKey)
        assertEquals(securityKey, vigenereCipher.getKey())

        val encryptText = vigenereCipher.encrypt(data = text)
        assertEquals(text, encryptText, "Encrypted empty text should be empty")

        val decryptText = vigenereCipher.decrypt(data = encryptText)
        assertEquals(text, decryptText, "Decrypted empty text should be empty")
    }

    @Test
    fun testLargeKeyEncryptionDecryption() {
        val text = "Welcome to my world"
        val securityKey = RandomUtil.generateStr(size = text.length * 10)
        val vigenereCipher = VigenereCipher(key = securityKey)
        assertEquals(securityKey, vigenereCipher.getKey())

        val encryptText = vigenereCipher.encrypt(data = text)
        assertNotEquals(text, encryptText, "Encrypted text should not match the original text")

        val decryptText = vigenereCipher.decrypt(data = encryptText)
        assertNotEquals(encryptText, decryptText, "Decrypted text should not match the encrypted text")

        val normalizedText = text.replace(" ", "").uppercase(Locale.getDefault())
        assertEquals(normalizedText, decryptText, "Decrypted normalized text should match the original text")
    }
}