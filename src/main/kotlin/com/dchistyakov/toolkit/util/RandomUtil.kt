package com.dchistyakov.toolkit.util

import java.security.SecureRandom

object RandomUtil {
    fun generateInt() = SecureRandom().nextInt(1, Int.MAX_VALUE % 100)

    fun generateInt(size: Int) = SecureRandom().nextInt(1, size)

    fun generateStr(): String {
        return (0..<generateInt())
            .map { LanguageUtil.ENG_ALPHABET.random() }
            .joinToString("")
    }

    fun generateStr(size: Int): String {
        return (0..<size)
            .map { LanguageUtil.ENG_ALPHABET.random() }
            .joinToString("")
    }
}