package jp.numero.dagashiapp

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.numero.dagashiapp.data.Theme
import jp.numero.dagashiapp.ui.rememberWindowSizeClass
import jp.numero.dagashiapp.ui.theme.DagashiAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        enableEdgeToEdge()

        setContent {
            val windowSizeClass = rememberWindowSizeClass()
            val navController = rememberNavController()
            val viewModel = hiltViewModel<MainViewModel>()
            val config by viewModel.config.collectAsState()
            val isDarkTheme = config.theme.isDarkTheme()
            DagashiAppTheme(
                isDarkTheme = isDarkTheme,
                dynamicColor = config.applyDynamicColor
            ) {
                DisposableEffect(isDarkTheme) {
                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.auto(
                            lightScrim = Color.TRANSPARENT,
                            darkScrim = Color.TRANSPARENT,
                            detectDarkMode = { isDarkTheme }
                        ),
                        navigationBarStyle = SystemBarStyle.auto(
                            lightScrim = lightScrim,
                            darkScrim = darkScrim,
                            detectDarkMode = { isDarkTheme }
                        ),
                    )
                    onDispose {}
                }
                Surface(
                    color = androidx.compose.ui.graphics.Color.Black
                ) {
                    DagashiNavigation(
                        navController = navController,
                        windowSizeClass = windowSizeClass,
                        modifier = Modifier.padding(
                            WindowInsets.safeDrawing
                                .only(WindowInsetsSides.Horizontal)
                                .asPaddingValues()
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun Theme.isDarkTheme(): Boolean {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    return when (this) {
        Theme.Light -> false
        Theme.Dark -> true
        Theme.FollowSystem -> isSystemInDarkTheme
    }
}

// ref: EdgeToEdge.kt
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)