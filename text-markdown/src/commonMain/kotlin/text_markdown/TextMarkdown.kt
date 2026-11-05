package com.m4ykey.text_markdown

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hrm.latex.renderer.LatexAutoWrap
import com.hrm.latex.renderer.model.LatexConfig
import com.hrm.latex.renderer.model.LatexTheme
import com.hrm.latex.renderer.model.LatexThemeColors
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeBlock
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeFence
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.rememberMarkdownState
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxThemes

@Composable
fun TextMarkdown(
    text : String,
    modifier: Modifier = Modifier,
    fontSize : TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    color : Color = LocalContentColor.current,
    textAlign: TextAlign = TextAlign.Start,
    alignment: Alignment = Alignment.Center
) {
    val isDarkTheme = isSystemInDarkTheme()

    val processedText = remember(text) {
        text
            .decodeAndCleanHtml()
            .fixImageReferences()
    }

    val segments = remember(processedText) { processedText.toSegments() }

    val latexConfig = remember(fontSize, color) {
        val colors = LatexThemeColors(color = color, backgroundColor = Color.Transparent)
        LatexConfig(
            fontSize = fontSize,
            theme = LatexTheme.auto(light = colors, dark = colors)
        )
    }

    val blockConfig = remember(latexConfig, fontSize) { latexConfig.copy(fontSize = fontSize * 1.15f) }

    val customComponents = remember(isDarkTheme) {
        markdownComponents(
            codeBlock = {
                val builder = remember (isDarkTheme) {
                    Highlights.Builder().theme(SyntaxThemes.atom(darkMode = isDarkTheme))
                }
                MarkdownHighlightedCodeBlock(
                    content = it.content,
                    node = it.node,
                    highlightsBuilder = builder
                )
            },
            codeFence = {
                val builder = remember (isDarkTheme) {
                    Highlights.Builder().theme(SyntaxThemes.atom(darkMode = isDarkTheme))
                }
                MarkdownHighlightedCodeFence(
                    content = it.content,
                    node = it.node,
                    highlightsBuilder = builder
                )
            }
        )
    }

    val linkColor = Color(0xFF1E88E5)
    val customTypography = markdownTypography(
        h1 = TextStyle(fontSize = fontSize * 1.6f, fontWeight = fontWeight),
        h2 = TextStyle(fontSize = fontSize * 1.4f, fontWeight = fontWeight),
        h3 = TextStyle(fontSize = fontSize * 1.2f, fontWeight = fontWeight),
        h4 = TextStyle(fontSize = fontSize * 1.1f, fontWeight = fontWeight),
        h5 = TextStyle(fontSize = fontSize, fontWeight = fontWeight),
        h6 = TextStyle(fontSize = fontSize * 0.9f, fontWeight = fontWeight),
        text = TextStyle(fontSize = fontSize, fontWeight = fontWeight, color = color, textAlign = textAlign),
        paragraph = TextStyle(fontSize = fontSize, fontWeight = fontWeight, color = color, textAlign = textAlign),
        ordered = TextStyle(fontSize = fontSize, fontWeight = fontWeight, color = color, textAlign = textAlign),
        bullet = TextStyle(fontSize = fontSize, fontWeight = fontWeight, color = color, textAlign = textAlign),
        list = TextStyle(fontSize = fontSize, fontWeight = fontWeight, color = color, textAlign = textAlign),
        quote = TextStyle(fontSize = fontSize, fontStyle = FontStyle.Italic),
        code = TextStyle(fontSize = fontSize * 0.85f, fontFamily = FontFamily.Monospace, lineHeight = fontSize * 1.2f),
        inlineCode = TextStyle(fontSize = fontSize * 0.85f, fontFamily = FontFamily.Monospace, lineHeight = fontSize * 1.2f),
        textLink = TextLinkStyles(
            style = SpanStyle(
                color = linkColor,
                textDecoration = TextDecoration.Underline
            )
        )
    )

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        SelectionContainer {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                segments.forEachIndexed { index, segment ->
                    key(index) {
                        when (segment) {
                            is Segment.Markdown -> {
                                val markdownState = rememberMarkdownState(
                                    content = segment.value.normalizeMarkdown(),
                                    retainState = false
                                )
                                Markdown(
                                    markdownState = markdownState,
                                    modifier = Modifier.wrapContentHeight(),
                                    imageTransformer = coil3ImageTransfer,
                                    components = customComponents,
                                    typography = customTypography
                                )
                            }
                            is Segment.Paragraph -> {
                                InlineMathParagraph(
                                    parts = segment.parts,
                                    config = latexConfig,
                                    fontSize = fontSize,
                                    fontWeight = fontWeight,
                                    color = color,
                                    textAlign = textAlign
                                )
                            }
                            is Segment.Block -> Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                LatexAutoWrap(
                                    latex = segment.latex,
                                    modifier = Modifier.fillMaxWidth(),
                                    config = blockConfig
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}