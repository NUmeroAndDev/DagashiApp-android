package jp.numero.dagashiapp.navigation

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun HomeScreen() {
}

@Destination(
    navArgsDelegate = MilestoneDetailScreenNavArgs::class
)
@Composable
fun MilestoneDetailScreen() {
}

data class MilestoneDetailScreenNavArgs(
    val path: String
)

@Destination
@Composable
fun SettingsScreen() {
}

@Destination
@Composable
fun LicensesScreen() {
}