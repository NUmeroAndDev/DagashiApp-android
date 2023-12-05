package jp.numero.dagashiapp.buildlogic.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project

class KspPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.devtools.ksp")
            }
        }
    }
}