package jp.numero.dagashiapp.buildlogic.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class ComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("kotlin-parcelize")
            }
            android {
                buildFeatures.compose = true
                composeOptions {
                    kotlinCompilerExtensionVersion =
                        libs.findVersion("composeCompiler").get().toString()
                }
            }
            dependencies {
                implementation(libs.findLibrary("androidx.compose.ui"))
                implementation(libs.findLibrary("androidx.compose.uiToolingPreview"))
                implementation(libs.findLibrary("androidx.compose.foundation"))
                implementation(libs.findLibrary("androidx.compose.materialIconsExtended"))
                implementation(libs.findLibrary("androidx.compose.material3"))
                implementation(libs.findLibrary("androidx.compose.material3WindowSizeClass"))

                debugImplementation(libs.findLibrary("androidx.compose.uiTooling"))
            }
        }
    }
}