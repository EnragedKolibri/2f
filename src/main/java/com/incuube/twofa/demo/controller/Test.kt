package com.incuube.twofa.demo.controller

fun main() {

    val source = "Your OTP code is [##**##*#]. Have a good day!"

    val codeMaskRegex = Regex("\\[[*#]{4,8}\\]")

    val validationRegex = Regex("(^[a-zA-Z\\s,.!?\\-]*\\[[*#]{4,8}\\][a-zA-Z\\s,.!?\\-]*)")

    validationRegex.findAll(source).forEach { println(it.value) }

    codeMaskRegex.findAll(source).forEach { println(it.value.trim('[', ']')) }

}