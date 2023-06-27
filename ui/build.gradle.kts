plugins {
    id("jp.numero.dagashiapp.buildlogic.primitive.androidlibrary")
    id("jp.numero.dagashiapp.buildlogic.primitive.kotlin")
    id("jp.numero.dagashiapp.buildlogic.primitive.hilt")
    id("jp.numero.dagashiapp.buildlogic.primitive.compose")
}

android {
    namespace = "jp.numero.dagashiapp.ui"
}

dependencies {
    implementation(projects.model)
    implementation(projects.repository)
    implementation(projects.navigation)

    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.window)

    implementation(libs.accompanist.systemUiController)
    implementation(libs.accompanist.swipeRefresh)

    implementation(libs.coil)

    implementation(libs.composeDestinations.core)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(libs.androidx.compose.uiTest)
}