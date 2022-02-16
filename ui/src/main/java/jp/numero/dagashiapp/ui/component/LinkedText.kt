package jp.numero.dagashiapp.ui.component

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString

private val symbolPattern by lazy {
    Regex("""(https?://[^\s\t\n]+)""")
}

typealias StringAnnotation = AnnotatedString.Range<String>

@Composable
fun LinkedText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
) {
    val uriHandler = LocalUriHandler.current
    val styledMessage = linkedTextFormatter(
        text = text,
        linkColor = MaterialTheme.colorScheme.tertiary
    )
    ClickableText(
        text = styledMessage,
        style = style,
        onClick = {
            styledMessage
                .getStringAnnotations(start = it, end = it)
                .firstOrNull()
                ?.let { annotation ->
                    uriHandler.openUri(annotation.item)
                }
        },
        modifier = modifier
    )
}

@Composable
private fun linkedTextFormatter(
    text: String,
    linkColor: Color
): AnnotatedString {
    val tokens = symbolPattern.findAll(text)

    return buildAnnotatedString {
        pushStyle(SpanStyle(color = LocalContentColor.current))
        var cursorPosition = 0

        for (token in tokens) {
            append(text.slice(cursorPosition until token.range.first))

            val annotatedString = AnnotatedString(
                text = token.value,
                spanStyle = SpanStyle(
                    color = linkColor
                )
            )
            append(annotatedString)

            val (item, start, end, tag) = StringAnnotation(
                item = token.value,
                start = token.range.first,
                end = token.range.last,
                tag = "LINK"
            )
            addStringAnnotation(tag = tag, start = start, end = end, annotation = item)

            cursorPosition = token.range.last + 1
        }

        if (!tokens.none()) {
            append(text.slice(cursorPosition..text.lastIndex))
        } else {
            append(text)
        }
    }
}