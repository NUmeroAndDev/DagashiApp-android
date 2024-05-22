package jp.numero.dagashiapp.ui.component

import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.flavours.gfm.GFMTokenTypes
import org.intellij.markdown.html.makeXssSafeDestination
import org.intellij.markdown.parser.MarkdownParser

@Composable
fun MarkdownText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
) {
    val styledMessage = markdownTextFormatter(
        text = text,
        linkColor = MaterialTheme.colorScheme.tertiary
    )
    BasicText(
        text = styledMessage,
        style = style,
        modifier = modifier
    )
}

@Composable
private fun markdownTextFormatter(
    text: String,
    linkColor: Color
): AnnotatedString {
    val flavour = remember { GFMFlavourDescriptor(makeHttpsAutoLinks = true) }
    val parsedTree = remember(text, flavour) {
        MarkdownParser(flavour).buildMarkdownTreeFromString(text)
    }
    return buildAnnotatedString {

        pushStyle(SpanStyle(color = LocalContentColor.current))

        appendFromParsedTree(parsedTree.children, linkColor) { start, end ->
            text.substring(start, end)
        }
    }
}

private fun AnnotatedString.Builder.appendFromParsedTree(
    nodes: List<ASTNode>,
    linkColor: Color,
    textProducer: (startIndex: Int, endIndex: Int) -> String,
) {
    nodes.forEach { node ->
        if (node.children.isNotEmpty()) {
            when (node.type) {
                MarkdownElementTypes.INLINE_LINK -> {
                    val label = node.findNodeByType(MarkdownElementTypes.LINK_TEXT)
                    val link = node.findNodeByType(MarkdownElementTypes.LINK_DESTINATION)
                    if (label == null || link == null) {
                        append(label?.let { textProducer(it.startOffset, it.endOffset) }.orEmpty())
                    } else {
                        val linkValue = textProducer(link.startOffset, link.endOffset)
                        withLink(
                            LinkAnnotation.Url(
                                url = makeXssSafeDestination(linkValue).toString(),
                                style = SpanStyle(color = linkColor)
                            )
                        ) {
                            append(
                                textProducer(
                                    label.startOffset,
                                    label.endOffset
                                ).let {
                                    // [value] -> value
                                    it.subSequence(1, it.length - 1).toString()
                                }
                            )
                        }
                    }
                }

                else -> {
                    appendFromParsedTree(node.children, linkColor, textProducer)
                }
            }
        } else {
            val value = textProducer(node.startOffset, node.endOffset)
            when (node.type) {
                GFMTokenTypes.GFM_AUTOLINK -> {
                    withLink(
                        LinkAnnotation.Url(
                            url = makeXssSafeDestination(value).toString(),
                            style = SpanStyle(color = linkColor)
                        )
                    ) {
                        append(value)
                    }
                }

                else -> {
                    append(value)
                }
            }
        }
    }
}

private fun ASTNode.findNodeByType(type: IElementType): ASTNode? {
    return children.firstOrNull { it.type == type }
}