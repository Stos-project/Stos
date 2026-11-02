package com.m4ykey.text_markdown

sealed interface Segment {
    data class Markdown(val value : String) : Segment
    data class Math(val latex : String) : Segment
}