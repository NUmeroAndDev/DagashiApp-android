package jp.numero.dagashiapp.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.ramcosta.composedestinations.navigation.navigateTo
import jp.numero.dagashiapp.navigation.destinations.LicensesScreenDestination
import jp.numero.dagashiapp.ui.R
import jp.numero.dagashiapp.ui.component.TopAppBar

@Composable
fun SettingsScreen(navController: NavHostController) {
    val viewModel: SettingsViewModel = hiltViewModel()
    SettingsScreen(
        appVersion = viewModel.appVersion,
        onBack = {
            navController.popBackStack()
        },
        onClickLicenses = {
            navController.navigateTo(LicensesScreenDestination)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    appVersion: AppVersion,
    onBack: () -> Unit,
    onClickLicenses: () -> Unit
) {
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.settings))
                },
                isCenterAlignedTitle = false,
                contentPadding = rememberInsetsPaddingValues(
                    LocalWindowInsets.current.statusBars,
                    applyBottom = false,
                ),
                scrollBehavior = scrollBehavior,
                onBack = onBack,
            )
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                SettingsContent(
                    appVersion = appVersion,
                    onClickLicenses = onClickLicenses,
                    modifier = Modifier.fillMaxSize()
                )
            }
        },
    )
}

@Composable
fun SettingsContent(
    appVersion: AppVersion,
    onClickLicenses: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = rememberInsetsPaddingValues(
            insets = LocalWindowInsets.current.systemBars,
            applyTop = false,
        )
    ) {
        item {
            SettingsItem(
                title = stringResource(id = R.string.application_version),
                summary = appVersion.name,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            SettingsItem(
                title = stringResource(id = R.string.licenses),
                modifier = Modifier.fillMaxWidth(),
                onClick = onClickLicenses
            )
        }
    }
}