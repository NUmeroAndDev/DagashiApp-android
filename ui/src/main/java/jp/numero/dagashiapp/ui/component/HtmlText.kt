package jp.numero.dagashiapp.ui.component

import android.os.Build
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.QuoteSpan
import android.widget.TextView
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.core.text.toSpannable

@Composable
fun HtmlText(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = LocalContentColor.current.copy(alpha = 0.60f),
    linkColor: Color = MaterialTheme.colorScheme.tertiary
) {
    val quoteStrokeWidth = with(LocalDensity.current) { 1.dp.roundToPx() }
    val quoteStrokeGap = with(LocalDensity.current) { 4.dp.roundToPx() }
    AndroidView(
        factory = { context ->
            TextView(context)
        },
        modifier = modifier
    ) { view ->
        view.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
            .toSpannable()
            .apply {
                val spans = getSpans(0, length, Any::class.java)
                spans.filterIsInstance<QuoteSpan>()
                    .forEach { span ->
                        // clear QuoteSpan
                        removeSpan(span)
                    }

                setSpan(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        QuoteSpan(textColor.toArgb(), quoteStrokeWidth, quoteStrokeGap)
                    } else {
                        QuoteSpan(textColor.toArgb())
                    },
                    0,
                    length,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
            }
        view.movementMethod = LinkMovementMethod.getInstance()
        view.setLinkTextColor(linkColor.toArgb())
        view.setTextColor(textColor.toArgb())
    }
}