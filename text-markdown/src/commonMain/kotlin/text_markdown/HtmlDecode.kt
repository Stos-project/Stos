package com.m4ykey.text_markdown

import com.mohamedrejeb.ksoup.entities.KsoupEntities
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser

private val blockRegex = Regex(
    """(```[\s\S]*?```|`[^`\n]+`|(?m)^(?: {4}|\t).*$)""" +
            """|\$\$([\s\S]+?)\$\$|(?<!\\)\\\[([\s\S]+?)\\]"""
)

private val inlineRegex = Regex(
    """(?<![\\$])\$(?![\s$])((?:\\.|[^$\\])+?)(?<![\s\\])\$(?!\d)|\\\((.+?)\\\)"""
)

private val tagLabelRegex = Regex("""\\tag\*?\{([^}]*)\}\s*\\label\{([^}]*)\}""")
private val eqrefRegex = Regex("""\\eqref\{([^}]*)\}""")
private val labelRegex = Regex("""\\label\{[^}]*\}""")

fun String.fixRefs(labels : Map<String, String>) : String =
    replace(eqrefRegex) { "\\text{(${labels[it.groupValues[1]] ?: "?"})}" }
        .replace(labelRegex, "")

fun String.toParagraphs() : List<String> {
    val result = mutableListOf<String>()
    val current = StringBuilder()
    var inFence = false
    fun flush() {
        if (current.isNotBlank()) result += current.toString().trimEnd()
        current.clear()
    }
    lines().forEach { line ->
        if (line.trimStart().startsWith("```")) inFence = !inFence
        if (line.isBlank() && !inFence) flush() else current.appendLine(line)
    }
    flush()
    return result
}

fun String.splitInline() : List<Part> {
    val parts = mutableListOf<Part>()
    var last = 0
    inlineRegex.findAll(this).forEach { m ->
        if (m.range.first > last) parts += Part.Text(substring(last, m.range.first))
        parts += Part.Math((m.groups[1]?.value ?: m.groups[2]?.value).orEmpty().trim())
        last = m.range.last + 1
    }
    if (last < length) parts += Part.Text(substring(last))
    return parts
}

fun String.toSegments() : List<Segment> {
    val labels = tagLabelRegex.findAll(this)
        .associate { it.groupValues[2] to it.groupValues[1] }
    val result = mutableListOf<Segment>()

    fun addMarkdown(text : String) {
        text.toParagraphs().forEach { p ->
            val isCode = p.contains("```") || p.startsWith("    ") || p.startsWith("\t")
            if (!isCode && inlineRegex.containsMatchIn(p)) {
                val flat = p.replace(Regex("""\s*\n\s*"""), " ")
                result += Segment.Paragraph(
                    flat.splitInline().map {
                        if (it is Part.Math) Part.Math(it.latex.fixRefs(labels)) else it
                    }
                )
            } else {
                val prev = result.lastOrNull()
                if (prev is Segment.Markdown) {
                    result[result.lastIndex] = Segment.Markdown(prev.value + "\n\n" + p)
                } else result += Segment.Markdown(p)
            }
        }
    }

    var textStart = 0
    blockRegex.findAll(this).forEach { m ->
        if (m.groups[1] != null) return@forEach
        addMarkdown(substring(textStart, m.range.first))
        val latex = (m.groups[2]?.value ?: m.groups[3]?.value).orEmpty().trim()
        if (latex.isNotEmpty()) result += Segment.Block(latex.fixRefs(labels))
        textStart = m.range.last + 1
    }
    addMarkdown(substring(textStart))
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