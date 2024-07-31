package com.ourmio.toolkit.encryption

/**
 * The Cipher interface defines operations for encryption and decryption algorithms.
 * Implementations of this interface provide methods to encrypt and decrypt data,
 * as well as retrieve the key used for encryption.
 */
interface Cipher {

    /**
     * Encrypts the given plaintext.
     *
     * @param data the plaintext to be encrypted.
     * @return the encrypted ciphertext.
     */
    fun encrypt(data: String): String

    /**
     * Decrypts the given ciphertext.
     *
     * @param data the ciphertext to be decrypted.
     * @return the decrypted plaintext.
     */
    fun decrypt(data: String): String

    /**
     * Retrieves the key used for encryption.
     *
     * @return the key object used for encryption.
     */
    fun getKey(): Any
}