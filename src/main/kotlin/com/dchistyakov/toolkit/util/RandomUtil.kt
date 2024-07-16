package com.dchistyakov.toolkit.util

import kotlin.random.Random

object RandomUtil {
    fun generateInt() = Random.nextInt(1, Int.MAX_VALUE % 100)

    fun generateInt(size: Int) = Random.nextInt(1, size)

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