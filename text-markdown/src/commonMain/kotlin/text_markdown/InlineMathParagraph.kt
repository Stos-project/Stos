package com.m4ykey.text_markdown

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.hrm.latex.renderer.measure.rememberLatexMeasurer
import com.hrm.latex.renderer.model.LatexConfig
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun InlineMathParagraph(
    parts : List<Part>,
    config : LatexConfig,
    fontSize : TextUnit,
    fontWeight: FontWeight,
    color : Color,
    textAlign: TextAlign
) {
    val measurer = rememberLatexMeasurer(config)

    val contents = parts.map { part ->
        if (part is Part.Math) measurer.inlineContent(part.latex, config) else null
    }

    val inline = mutableMapOf<String, InlineTextContent>()
    val text = buildAnnotatedString {
        parts.forEachIndexed { index, part ->
            when (part) {
                is Part.Text -> appendSimpleMarkdown(part.value)
                is Part.Math -> {
                    val content = contents[index]
                    if (content != null) {
                        inline["m$index"] = content
                        appendInlineContent("m$index", part.latex)
                    } else append("$${part.latex}$")
                }
            }
        }
    }

    Text(
        text = text,
        inlineContent = inline,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        lineHeight = fontSize * 1.6f,
        textAlign = textAlign
    )
}

private val simpleMdRegex = Regex("""\*\*(.+?)\*\*|`([^`]+)`""")

private fun AnnotatedString.Builder.appendSimpleMarkdown(text : String) {
    var last = 0
    simpleMdRegex.findAll(text).forEach { m ->
        append(text.substring(last, m.range.first))
        if (m.groups[1] != null) {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(m.groupValues[1]) }
        } else {
            withStyle(SpanStyle(fontFamily = FontFamily.Monospace)) { append(m.groupValues[2]) }
        }
        last = m.range.last + 1
    }
    append(text.substring(last))
}