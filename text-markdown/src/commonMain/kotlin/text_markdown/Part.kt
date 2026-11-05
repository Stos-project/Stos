package com.m4ykey.text_markdown

sealed interface Part {
    data class Text(val value : String) : Part
    data class Math(val latex : String) : Part
}