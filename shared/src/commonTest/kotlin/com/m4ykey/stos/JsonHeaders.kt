package com.m4ykey.stos

import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf

fun jsonHeaders() = headersOf(
    HttpHeaders.ContentType,
    "application/json"
)