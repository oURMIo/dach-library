package com.ourmio.toolkit.encryption

import com.ourmio.toolkit.exception.BadSecretKeyException
import java.util.Base64
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * AESCipher is a utility class for encrypting and decrypting data using the AES algorithm.
 *
 * @property key the secret key used for encryption and decryption.
 * @constructor Creates an AESCipher instance with the specified secret key.
 * @throws BadSecretKeyException if the provided key algorithm is not "AES".
 */
class AESCipher(private val key: SecretKey) : Cipher {
    private val encryptCipher = javax.crypto.Cipher.getInstance("AES/ECB/PKCS5Padding")
    private val decryptCipher = javax.crypto.Cipher.getInstance("AES/ECB/PKCS5Padding")

    init {
        require(key.algorithm == "AES") {
            throw BadSecretKeyException("Received wrong secret key")
        }
        encryptCipher.apply {
            init(javax.crypto.Cipher.ENCRYPT_MODE, key)
        }
        decryptCipher.apply {
            init(javax.crypto.Cipher.DECRYPT_MODE, key)
        }
    }

    /**
     * Encrypts the given data using the AES algorithm.
     *
     * @param data the data to be encrypted.
     * @return the encrypted data encoded in Base64.
     */
    override fun encrypt(data: String): String {
        return if (data.isEmpty()) "" else {
            val encryptedBytes = encryptCipher.doFinal(data.toByteArray())
            Base64.getEncoder().encodeToString(encryptedBytes)
        }
    }

    /**
     * Decrypts the given data using the AES algorithm.
     *
     * @param data the data to be decrypted.
     * @return the decrypted data.
     */
    override fun decrypt(data: String): String {
        return if (data.isEmpty()) "" else {
            val decryptedBytes = decryptCipher.doFinal(Base64.getDecoder().decode(data))
            String(decryptedBytes)
        }
    }

    /**
     * Returns the secret key used for encryption and decryption.
     *
     * @return the secret key.
     */
    override fun getKey() = key

    /**
     * Encrypts the given data using the provided secret key.
     *
     * @param data the data to be encrypted.
     * @param secretKey the secret key used for encryption.
     * @return the encrypted data encoded in Base64.
     */
    fun encrypt(data: String, secretKey: SecretKey): String {
        return if (data.isEmpty()) "" else {
            val cipher = javax.crypto.Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, secretKey)
            val encryptedBytes = cipher.doFinal(data.toByteArray())
            Base64.getEncoder().encodeToString(encryptedBytes)
        }
    }

    /**
     * Decrypts the given data using the provided secret key.
     *
     * @param data the data to be decrypted.
     * @param secretKey the secret key used for decryption.
     * @return the decrypted data.
     */
    fun decrypt(data: String, secretKey: SecretKey): String {
        return if (data.isEmpty()) "" else {
            val cipher = javax.crypto.Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, secretKey)
            val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(data))
            String(decryptedBytes)
        }
    }

    companion object {
        /**
         * Generates a secret key for the AES algorithm with the specified key size.
         *
         * @param keySize the size of the key in bits (e.g., 128, 192, 256).
         * @return the generated secret key.
         */
        private fun generateKey(keySize: Int): SecretKey {
            return KeyGenerator.getInstance("AES").apply { init(keySize) }.generateKey()
        }

        /**
         * Generates a 128-bit AES secret key.
         *
         * @return the generated secret key.
         */
        fun generate128Key(): SecretKey = generateKey(128)

        /**
         * Generates a 192-bit AES secret key.
         *
         * @return the generated secret key.
         */
        fun generate192Key(): SecretKey = generateKey(192)

        /**
         * Generates a 256-bit AES secret key.
         *
         * @return the generated secret key.
         */
        fun generate256Key(): SecretKey = generateKey(256)
    }
}