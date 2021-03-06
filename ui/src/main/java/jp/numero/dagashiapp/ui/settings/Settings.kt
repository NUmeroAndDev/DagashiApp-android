package jp.numero.dagashiapp.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Contrast
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.navigation.navigateTo
import jp.numero.dagashiapp.model.Config
import jp.numero.dagashiapp.model.Theme
import jp.numero.dagashiapp.navigation.destinations.LicensesScreenDestination
import jp.numero.dagashiapp.ui.R
import jp.numero.dagashiapp.ui.component.TopAppBar

@Composable
fun SettingsScreen(navController: NavHostController) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val config by viewModel.config.collectAsState()
    SettingsScreen(
        config = config,
        appVersion = viewModel.appVersion,
        onBack = {
            navController.popBackStack()
        },
        onToggleApplyDynamicColor = {
            viewModel.updateApplyDynamicColor(it)
        },
        onSelectTheme = {
            viewModel.updateTheme(it)
        },
        onClickLicenses = {
            navController.navigateTo(LicensesScreenDestination)
        }
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
    val state = rememberTopAppBarScrollState()
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior(state) }
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .windowInsetsPadding(
                WindowInsets.displayCutout
                    .only(WindowInsetsSides.Horizontal)
            ),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.settings))
                },
                isCenterAlignedTitle = false,
                contentPadding = WindowInsets.statusBars
                    .only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                    .asPaddingValues(),
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
        contentPadding = WindowInsets.systemBars
            .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)
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
                    title = stringResource(id = R.string.apply_dynamic_color),
                    trailing = {
                        Switch(
                            checked = config.applyDynamicColor,
                            onCheckedChange = {
                                onToggleApplyDynamicColor(it)
                            }
                        )
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Palette,
                            contentDescription = null
                        )
                    },
                    onClick = {
                        onToggleApplyDynamicColor(!config.applyDynamicColor)
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
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

@Composable
fun SelectThemeSettingsItem(
    currentTheme: Theme,
    onSelectTheme: (Theme) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpandedMenu by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        SettingsItem(
            title = stringResource(id = R.string.app_theme),
            summary = stringResource(id = currentTheme.titleRes),
            onClick = {
                isExpandedMenu = true
            },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Contrast,
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
                Icon(imageVector = Icons.Outlined.Check, contentDescription = null)
            }
        },
        modifier = modifier,
    )
}

private val Theme.titleRes: Int
    get() = when (this) {
        Theme.Light -> R.string.light_theme
        Theme.Dark -> R.string.dark_theme
        Theme.FollowSystem -> R.string.follow_device
    }