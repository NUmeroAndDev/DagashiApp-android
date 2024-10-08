package jp.numero.dagashiapp.feature.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import jp.numero.dagashiapp.data.Config
import jp.numero.dagashiapp.data.Theme
import jp.numero.dagashiapp.ui.UiDrawables
import jp.numero.dagashiapp.ui.UiStrings
import jp.numero.dagashiapp.ui.component.TopAppBar

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onClickLicenses: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val config by viewModel.config.collectAsState()
    SettingsScreen(
        config = config,
        appVersion = viewModel.appVersion,
        onBack = onBack,
        onToggleApplyDynamicColor = {
            viewModel.updateApplyDynamicColor(it)
        },
        onSelectTheme = {
            viewModel.updateTheme(it)
        },
        onClickLicenses = onClickLicenses
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    config: Config,
    appVersion: AppVersion,
    onBack: () -> Unit,
    onToggleApplyDynamicColor: (Boolean) -> Unit,
    onSelectTheme: (Theme) -> Unit,
    onClickLicenses: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = UiStrings.settings))
                },
                isCenterAlignedTitle = false,
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
                    config = config,
                    appVersion = appVersion,
                    onSelectTheme = onSelectTheme,
                    onToggleApplyDynamicColor = onToggleApplyDynamicColor,
                    onClickLicenses = onClickLicenses,
                    modifier = Modifier.fillMaxSize()
                )
            }
        },
    )
}

@Composable
fun SettingsContent(
    config: Config,
    appVersion: AppVersion,
    onSelectTheme: (Theme) -> Unit,
    onToggleApplyDynamicColor: (Boolean) -> Unit,
    onClickLicenses: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = WindowInsets.safeDrawing
            .only(WindowInsetsSides.Bottom)
            .asPaddingValues()
    ) {
        item {
            SelectThemeSettingsItem(
                currentTheme = config.theme,
                onSelectTheme = onSelectTheme
            )
        }
        if (Config.enableDynamicColor) {
            item {
                SettingsItem(
                    title = stringResource(id = UiStrings.apply_dynamic_color),
                    trailing = {
                        Switch(
                            checked = config.applyDynamicColor,
                            onCheckedChange = null
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(UiDrawables.ic_palette),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .toggleable(
                            value = config.applyDynamicColor,
                            onValueChange = {
                                onToggleApplyDynamicColor(it)
                            }
                        ),
                )
            }
        }
        item {
            SettingsItem(
                title = stringResource(id = UiStrings.application_version),
                summary = appVersion.name,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            SettingsItem(
                title = stringResource(id = UiStrings.licenses),
                modifier = Modifier.fillMaxWidth(),
                onClick = onClickLicenses
            )
        }
    }
}

@Composable
fun SelectThemeSettingsItem(
    currentTheme: Theme,
    onSelectTheme: (Theme) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpandedMenu by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        SettingsItem(
            title = stringResource(id = UiStrings.app_theme),
            summary = stringResource(id = currentTheme.titleRes),
            onClick = {
                isExpandedMenu = true
            },
            icon = {
                Icon(
                    painter = painterResource(UiDrawables.ic_contrast),
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = isExpandedMenu,
            onDismissRequest = {
                isExpandedMenu = false
            }
        ) {
            Theme.toList().forEach {
                ThemeDropdownItem(
                    isSelected = it == currentTheme,
                    theme = it,
                    onClick = {
                        isExpandedMenu = false
                        onSelectTheme(it)
                    }
                )
            }
        }
    }
}

@Composable
fun ThemeDropdownItem(
    isSelected: Boolean,
    theme: Theme,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenuItem(
        text = {
            Text(text = stringResource(id = theme.titleRes))
        },
        onClick = onClick,
        leadingIcon = {
            if (isSelected) {
                Icon(painter = painterResource(UiDrawables.ic_check), contentDescription = null)
            }
        },
        modifier = modifier,
    )
}

private val Theme.titleRes: Int
    get() = when (this) {
        Theme.Light -> UiStrings.light_theme
        Theme.Dark -> UiStrings.dark_theme
        Theme.FollowSystem -> UiStrings.follow_device
    }