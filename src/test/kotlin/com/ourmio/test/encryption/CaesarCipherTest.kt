package com.ourmio.test.encryption

import com.ourmio.toolkit.encryption.CaesarCipher
import com.ourmio.toolkit.util.RandomUtil
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class CaesarCipherTest {
    @ParameterizedTest(name = "Test encryption and decryption with key size {1}")
    @CsvSource(
        "Welcome to my world, 10",
        "Welcome to my world, 21",
        "Welcome to my world, 40"
    )
    fun testEncryptionDecryptionWithVariousKeySizes(text: String, keySize: Int) {
        val securityKey = RandomUtil.generateInt(keySize)
        val caesarCipher = CaesarCipher(key = securityKey)
        assertEquals(securityKey, caesarCipher.getKey())

        val encryptText = caesarCipher.encrypt(data = text)
        assertNotEquals(text, encryptText, "Encrypted text should not match the original text")

        val decryptText = caesarCipher.decrypt(data = encryptText)
        assertNotEquals(encryptText, decryptText, "Decrypted text should not match the encrypted text")
        assertEquals(text, decryptText, "Decrypted text should match the original text")
    }

    @Test
    fun testEmptyStringEncryptionDecryption() {
        val text = ""
        val securityKey = CaesarCipher.generateKey()
        val caesarCipher = CaesarCipher(key = securityKey)
        assertEquals(securityKey, caesarCipher.getKey())

        val encryptText = caesarCipher.encrypt(data = text)
        assertEquals(text, encryptText, "Encrypted empty text should be empty")

        val decryptText = caesarCipher.decrypt(data = encryptText)
        assertEquals(text, decryptText, "Decrypted empty text should be empty")
    }

    @Test
    fun testLargeKeyEncryptionDecryption() {
        val text = "Welcome to my world"
        val securityKey = RandomUtil.generateInt(text.length * 10)
        val caesarCipher = CaesarCipher(key = securityKey)
        assertEquals(securityKey, caesarCipher.getKey())

        val encryptText = caesarCipher.encrypt(data = text)
        assertNotEquals(text, encryptText, "Encrypted text should not match the original text")

        val decryptText = caesarCipher.decrypt(data = encryptText)
        assertNotEquals(encryptText, decryptText, "Decrypted text should not match the encrypted text")
        assertEquals(text, decryptText, "Decrypted text should match the original text")
    }
}