package jp.numero.dagashiapp.ui

import jp.takuji31.compose.navigation.screen.annotation.AutoScreenId
import jp.takuji31.compose.navigation.screen.annotation.Route
import jp.takuji31.compose.navigation.screen.annotation.StringArgument

@AutoScreenId("Screen")
enum class DagashiScreenId {
    @Route(
        route = "/",
    )
    Home,

    @Route(
        route = "/milestoneDetail/{path}",
        stringArguments = [
            StringArgument("path"),
        ],
    )
    MilestoneDetail,

    @Route(
        route = "/settings"
    )
    Settings,
}