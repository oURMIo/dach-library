package com.dchistyakov.toolkit.encryption

import com.dchistyakov.toolkit.exception.BadSecretKeyException
import java.util.Base64
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * DESCipher is a utility class for encrypting and decrypting data using the DES algorithm.
 *
 * @property key the secret key used for encryption and decryption.
 * @constructor Creates a DESCipher instance with the specified secret key.
 * @throws BadSecretKeyException if the provided key algorithm is not "DES".
 */
class DESCipher(private val key: SecretKey) : Cipher {
    private val encryptCipher = javax.crypto.Cipher.getInstance("DES/ECB/PKCS5Padding")
    private val decryptCipher = javax.crypto.Cipher.getInstance("DES/ECB/PKCS5Padding")

    init {
        require(key.algorithm == "DES") {
            throw BadSecretKeyException("Received wrong secret key")
        }
        encryptCipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key)
        decryptCipher.init(javax.crypto.Cipher.DECRYPT_MODE, key)
    }

    /**
     * Encrypts the given data using the DES algorithm.
     *
     * @param data the data to be encrypted.
     * @return the encrypted data encoded in Base64.
     */
    override fun encrypt(data: String): String {
        if (data.isEmpty()) {
            return ""
        }

        val encryptedBytes = encryptCipher.doFinal(data.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    /**
     * Decrypts the given data using the DES algorithm.
     *
     * @param data the data to be decrypted.
     * @return the decrypted data.
     */
    override fun decrypt(data: String): String {
        if (data.isEmpty()) {
            return ""
        }

        val decryptedBytes = decryptCipher.doFinal(Base64.getDecoder().decode(data))
        return String(decryptedBytes)
    }

    /**
     * Returns the secret key used for encryption and decryption.
     *
     * @return the secret key.
     */
    override fun getKey() = key

    /**
     * Encrypts the given data using the provided secret key and the DES algorithm.
     *
     * @param data the data to be encrypted.
     * @param secretKey the secret key used for encryption.
     * @return the encrypted data encoded in Base64.
     */
    fun encrypt(data: String, secretKey: SecretKey): String {
        if (data.isEmpty()) {
            return ""
        }

        val cipher = javax.crypto.Cipher.getInstance("DES/ECB/PKCS5Padding")
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(data.toByteArray())
        return Base64.getEncoder().encodeToString(encryptedBytes)
    }

    /**
     * Decrypts the given data using the provided secret key and the DES algorithm.
     *
     * @param data the data to be decrypted.
     * @param secretKey the secret key used for decryption.
     * @return the decrypted data.
     */
    fun decrypt(data: String, secretKey: SecretKey): String {
        if (data.isEmpty()) {
            return ""
        }

        val cipher = javax.crypto.Cipher.getInstance("DES/ECB/PKCS5Padding")
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, secretKey)
        val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(data))
        return String(decryptedBytes)
    }

    companion object {
        /**
         * Generates a random 56-bit DES secret key.
         *
         * @return the generated secret key.
         */
        fun generateKey(): SecretKey {
            val keyGen = KeyGenerator.getInstance("DES")
            keyGen.init(56/* Special size */)
            return keyGen.generateKey()
        }
    }
}