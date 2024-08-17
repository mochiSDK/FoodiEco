package com.foodieco.utils

import java.security.MessageDigest

@OptIn(ExperimentalStdlibApi::class)
fun String.toSha256(): String = MessageDigest.getInstance("SHA-256")
    .digest(toByteArray())
    .toHexString()
