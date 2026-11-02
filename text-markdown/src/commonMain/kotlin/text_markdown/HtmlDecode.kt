package com.m4ykey.text_markdown

import com.mohamedrejeb.ksoup.entities.KsoupEntities
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser

private val mathRegex = Regex(
    """(```[\s\S]*?```|`[^`\n]+`|(?m)^(?: {4}|\t).*$)""" +
            """|\$\$([\s\S]+?)\$\$""" +
            """|(?<!\\)\\\[([\s\S]+?)\\]""" +
            """|(\\begin\{(align\*?|aligned|gather\*?|equation\*?|cases|[pbvBV]?matrix)\}[\s\S]+?\\end\{\5\})"""
)

fun String.splitMath() : List<Segment> {
    val result = mutableListOf<Segment>()
    var textStart = 0

    mathRegex.findAll(this).forEach { match ->
        if (match.groups[1] != null) return@forEach

        val before = substring(textStart, match.range.first)
        if (before.isNotBlank()) result += Segment.Markdown(before)

        val latex = (match.groups[2]?.value ?: match.groups[3]?.value).orEmpty().trim()
        if (latex.isNotEmpty()) result += Segment.Math(latex)

        textStart = match.range.last + 1
    }

    val rest = substring(textStart)
    if (rest.isNotBlank()) result += Segment.Markdown(rest)
    return result
}

fun String.decodeAndCleanHtml() : String {
    val result = StringBuilder()

    val handler = KsoupHtmlHandler.Builder()
        .onText { text ->
            result.append(text)
        }
        .onOpenTag { name, _, _ ->
            when (name) {
                "p", "br", "div", "li" -> result.append("\n")
            }
        }
        .onCloseTag { name, _ ->
            if (name == "p" || name == "div") {
                result.append("\n")
            }
        }
        .build()

    val parser = KsoupHtmlParser(handler = handler)

    parser.write(this)
    parser.end()

    return KsoupEntities
        .decodeHtml(result.toString())
        .replace(Regex("\n{3,}"), "\n\n")
        .trim()
}

fun String.normalizeMarkdown() : String {
    val cleaned = this
        .replace("\r\n", "\n")
        .replace("\r", "\n")

    val lines = cleaned.lines()
    val result = StringBuilder()

    for (i in lines.indices) {
        val currentLines = lines[i]
        val isIndentedCode = currentLines.startsWith("    ") || currentLines.startsWith("\t")

        if (isIndentedCode && i > 0) {
            val previousLine = lines[i - 1]
            if (previousLine.trim().isNotEmpty() && !previousLine.startsWith("    ")) {
                result.append("\n")
            }
        }
        result.append(currentLines).append("\n")
    }

    return result.toString()
}

fun String.fixImageReferences(): String {
    val referenceRegex = Regex("""^\[(\d+)]\s*:\s*(.+)$""", RegexOption.MULTILINE)
    val references = mutableMapOf<String, String>()

    referenceRegex.findAll(this).forEach { match ->
        references[match.groupValues[1]] = match.groupValues[2].trim()
    }

    var result = this

    val fullRef = Regex("""\[!\[(.*?)]\[(\d+)]]\[(\d+)]""")
    result = result.replace(fullRef) {
        val alt = it.groupValues[1]
        val ref = references[it.groupValues[2]] ?: return@replace it.value
        "![$alt]($ref)"
    }

    val shortRef = Regex("""\[!\[(.*?)]\[(\d+)]]""")
    result = result.replace(shortRef) {
        val alt = it.groupValues[1]
        val ref = references[it.groupValues[2]] ?: return@replace it.value
        "![$alt]($ref)"
    }

    return result
}