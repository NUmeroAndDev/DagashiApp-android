package jp.numero.dagashiapp.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

private val symbolPattern by lazy {
    Regex("""<blockquote(.*?)</blockquote>""")
}

@Composable
fun IssueDescriptionText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        val tokens = symbolPattern.findAll(text)
        var cursorPosition = 0

        for (token in tokens) {
            MarkdownText(
                text = text.slice(cursorPosition until token.range.first),
                style = MaterialTheme.typography.bodyMedium
            )
            HtmlText(text = token.value)
            cursorPosition = token.range.last + 1
        }

        if (!tokens.none()) {
            MarkdownText(
                text = text.slice(cursorPosition..text.lastIndex),
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            MarkdownText(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}