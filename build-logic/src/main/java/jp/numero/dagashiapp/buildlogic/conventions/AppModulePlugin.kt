package jp.numero.dagashiapp.buildlogic.conventions

import jp.numero.dagashiapp.buildlogic.primitive.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AppModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("jp.numero.dagashiapp.buildlogic.primitive.androidapplication")
                apply("jp.numero.dagashiapp.buildlogic.primitive.kotlin")
                apply("jp.numero.dagashiapp.buildlogic.primitive.compose")
                apply("jp.numero.dagashiapp.buildlogic.primitive.hilt")
            }
            dependencies {
                implementation(project(":model"))
                implementation(project(":data"))
                implementation(project(":ui"))
            }
        }
    }
}