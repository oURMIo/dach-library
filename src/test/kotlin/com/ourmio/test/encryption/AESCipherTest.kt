package com.ourmio.test.encryption

import com.ourmio.toolkit.encryption.AESCipher
import javax.crypto.SecretKey
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class AESCipherTest {
    enum class KeySize(val generator: () -> SecretKey) {
        KEY_128(AESCipher::generate128Key),
        KEY_192(AESCipher::generate192Key),
        KEY_256(AESCipher::generate256Key)
    }

    @ParameterizedTest(name = "Test AES encryption and decryption with {0}")
    @EnumSource(KeySize::class)
    fun testEncryptionDecryptionWithVariousKeySizes(keySize: KeySize) {
        val text = "Welcome to my world"
        val securityKey = keySize.generator()
        val aesCipher = AESCipher(key = securityKey)
        assertEquals(securityKey, aesCipher.getKey())

        val encryptText = aesCipher.encrypt(data = text)
        assertNotEquals(text, encryptText, "Encrypted text should not match the original text")

        val decryptText = aesCipher.decrypt(data = encryptText)
        assertNotEquals(encryptText, decryptText, "Decrypted text should not match the encrypted text")
        assertEquals(text, decryptText, "Decrypted text should match the original text")
    }

    @Test
    fun testEmptyStringEncryptionDecryption() {
        val text = ""
        val securityKey = AESCipher.generate128Key()
        val aesCipher = AESCipher(key = securityKey)
        assertEquals(securityKey, aesCipher.getKey())

        val encryptText = aesCipher.encrypt(data = text)
        assertEquals(text, encryptText, "Encrypted empty text should be empty")

        val decryptText = aesCipher.decrypt(data = encryptText)
        assertEquals(text, decryptText, "Decrypted empty text should be empty")
    }

    @Test
    fun testLargeKeyEncryptionDecryption() {
        val text = "Welcome to my world"
        val securityKey = AESCipher.generate256Key()
        val aesCipher = AESCipher(key = securityKey)
        assertEquals(securityKey, aesCipher.getKey())

        val encryptText = aesCipher.encrypt(data = text)
        assertNotEquals(text, encryptText, "Encrypted text should not match the original text")

        val decryptText = aesCipher.decrypt(data = encryptText)
        assertNotEquals(encryptText, decryptText, "Decrypted text should not match the encrypted text")
        assertEquals(text, decryptText, "Decrypted text should match the original text")
    }
}