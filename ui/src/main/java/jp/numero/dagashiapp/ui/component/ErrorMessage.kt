package jp.numero.dagashiapp.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import jp.numero.dagashiapp.ui.R

@Composable
fun ErrorMessage(
    error: Throwable,
    modifier: Modifier = Modifier,
    action: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.load_error_message),
            style = MaterialTheme.typography.titleLarge
        )
        if (action != null) {
            Spacer(modifier = Modifier.size(16.dp))
            action()
        }
    }
}