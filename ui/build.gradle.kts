plugins {
    id("jp.numero.dagashiapp.buildlogic.primitive.androidlibrary")
    id("jp.numero.dagashiapp.buildlogic.primitive.kotlin")
    id("jp.numero.dagashiapp.buildlogic.primitive.compose")
}

android {
    namespace = "jp.numero.dagashiapp.ui"
}

dependencies {
    implementation(projects.data)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.window)

    implementation(libs.coil)

    implementation(libs.markdown)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    androidTestImplementation(libs.androidx.compose.uiTest)
}