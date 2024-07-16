package com.dchistyakov.test.encryption

import com.dchistyakov.toolkit.encryption.DESCipher
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import org.junit.jupiter.api.Test

class DESCipherTest {

    @Test
    fun testEncryptionDecryption() {
        val text = "Welcome to my world"
        val securityKey = DESCipher.generateKey()
        val matrixCipher = DESCipher(key = securityKey)
        assertEquals(securityKey, matrixCipher.getKey())

        val encryptText = matrixCipher.encrypt(data = text)
        assertNotEquals(text, encryptText, "Encrypted text should not match the original text")

        val decryptText = matrixCipher.decrypt(data = encryptText)
        assertNotEquals(encryptText, decryptText, "Decrypted text should not match the encrypted text")
        assertEquals(text, decryptText, "Decrypted text should match the original text")
    }

    @Test
    fun testEmptyStringEncryptionDecryption() {
        val text = ""
        val securityKey = DESCipher.generateKey()
        val aesCipher = DESCipher(key = securityKey)
        assertEquals(securityKey, aesCipher.getKey())

        val encryptText = aesCipher.encrypt(data = text)
        assertEquals(text, encryptText, "Encrypted empty text should be empty")

        val decryptText = aesCipher.decrypt(data = encryptText)
        assertEquals(text, decryptText, "Decrypted empty text should be empty")
    }
}