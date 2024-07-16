package com.dchistyakov.test.encryption

import com.dchistyakov.toolkit.encryption.VernamCipher
import com.dchistyakov.toolkit.util.RandomUtil
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class VernamCipherTest {

    @ParameterizedTest(name = "Test encryption and decryption with key size {1}")
    @CsvSource(
        "Welcome to my world, 10",
        "Welcome to my world, 21",
        "Welcome to my world, 40"
    )
    fun testEncryptionDecryptionWithVariousKeySizes(text: String, keySize: Int) {
        val securityKey = RandomUtil.generateStr(size = keySize)
        val vernamCipher = VernamCipher(key = securityKey)
        assertEquals(securityKey, vernamCipher.getKey())

        val encryptText = vernamCipher.encrypt(data = text)
        assertNotEquals(text, encryptText, "Encrypted text should not match the original text")

        val decryptText = vernamCipher.decrypt(data = encryptText)
        assertNotEquals(encryptText, decryptText, "Decrypted text should not match the encrypted text")
        assertEquals(text, decryptText, "Decrypted text should match the original text")
    }

    @Test
    fun testEmptyStringEncryptionDecryption() {
        val text = ""
        val securityKey = VernamCipher.generateKey()
        val vernamCipher = VernamCipher(key = securityKey)
        assertEquals(securityKey, vernamCipher.getKey())

        val encryptText = vernamCipher.encrypt(data = text)
        assertEquals(text, encryptText, "Encrypted empty text should be empty")

        val decryptText = vernamCipher.decrypt(data = encryptText)
        assertEquals(text, decryptText, "Decrypted empty text should be empty")
    }

    @Test
    fun testLargeKeyEncryptionDecryption() {
        val text = "Welcome to my world"
        val securityKey = RandomUtil.generateStr(size = text.length * 10)
        val vernamCipher = VernamCipher(key = securityKey)
        assertEquals(securityKey, vernamCipher.getKey())

        val encryptText = vernamCipher.encrypt(data = text)
        assertNotEquals(text, encryptText, "Encrypted text should not match the original text")

        val decryptText = vernamCipher.decrypt(data = encryptText)
        assertNotEquals(encryptText, decryptText, "Decrypted text should not match the encrypted text")
        assertEquals(text, decryptText, "Decrypted text should match the original text")
    }
}