package jp.numero.dagashiapp.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.numero.dagashiapp.ui.R

@Composable
fun DarkThemeItem(
    modifier: Modifier = Modifier
) {
    // TODO apply current theme
    SettingsItem(
        title = "DarkTheme",
        summary = "Light",
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_contrast),
                contentDescription = null
            )
        },
        onClick = {},
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun SettingsItem(
    title: String,
    modifier: Modifier = Modifier,
    summary: String? = null,
    icon: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .clickable(enabled = onClick != null) {
                onClick?.invoke()
            }
            .heightIn(min = 46.dp)
            .padding(vertical = 12.dp),
    ) {
        Box(
            modifier = Modifier.width(56.dp),
            contentAlignment = Alignment.Center
        ) {
            if (icon != null) {
                icon()
            }
        }
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
            if (summary != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = summary,
                    style = MaterialTheme.typography.labelMedium,
                    color = LocalContentColor.current.copy(alpha = 0.54f)
                )
            }
        }
        Box(
            modifier = Modifier.padding(end = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (trailing != null) {
                trailing()
            }
        }
    }
}

@Preview
@Composable
fun PreviewDarkThemeItem() {
    DarkThemeItem()
}

@Preview
@Composable
fun PreviewSettingsItem() {
    SettingsItem(
        title = "Title",
        summary = "Summary",
        onClick = {}
    )
}