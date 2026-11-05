package com.m4ykey.text_markdown

sealed interface Segment {
    data class Markdown(val value : String) : Segment
    data class Paragraph(val parts : List<Part>) : Segment
    data class Block(val latex : String) : Segment
}