plugins {
    id("jp.numero.dagashiapp.buildlogic.conventions.featuremodule")
}

android {
    namespace = "jp.numero.dagashiapp.feature.milestones"
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.window)

    implementation(libs.androidx.compose.material3Adaptive)
    implementation(libs.androidx.compose.material3AdaptiveLayout)
    implementation(libs.androidx.compose.material3AdaptiveNavigation)

    implementation(libs.coil)
}