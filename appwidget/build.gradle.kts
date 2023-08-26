plugins {
    id("jp.numero.dagashiapp.buildlogic.primitive.androidlibrary")
    id("jp.numero.dagashiapp.buildlogic.primitive.kotlin")
    id("jp.numero.dagashiapp.buildlogic.primitive.hilt")
}

android {
    namespace = "jp.numero.dagashiapp.appwidget"
    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
    implementation(projects.model)
    implementation(projects.data)
    implementation(projects.ui)

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.work)
    api(libs.androidx.hilt.work)
    kapt(libs.androidx.hilt.compiler)
    api(libs.androidx.glance.widget)
    implementation(libs.androidx.glance.widget.material3)

    testImplementation("junit:junit:4.13.2")
}