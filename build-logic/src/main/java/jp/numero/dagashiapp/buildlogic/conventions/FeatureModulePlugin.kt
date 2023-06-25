package jp.numero.dagashiapp.buildlogic.conventions

import org.gradle.api.Plugin
import org.gradle.api.Project

class FeatureModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("jp.numero.dagashiapp.buildlogic.primitive.androidlibrary")
                apply("jp.numero.dagashiapp.buildlogic.primitive.kotlin")
                apply("jp.numero.dagashiapp.buildlogic.primitive.compose")
                apply("jp.numero.dagashiapp.buildlogic.primitive.hilt")
            }
        }
    }
}