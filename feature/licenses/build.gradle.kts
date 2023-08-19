plugins {
    id("jp.numero.dagashiapp.buildlogic.conventions.featuremodule")
}

android {
    namespace = "jp.numero.dagashiapp.feature.licenses"
}

dependencies {
    implementation(projects.ui)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.licenses)
}