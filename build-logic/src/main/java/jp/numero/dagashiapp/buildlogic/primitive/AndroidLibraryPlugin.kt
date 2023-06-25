package jp.numero.dagashiapp.buildlogic.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }

            library {
                commonConfiguration()

                defaultConfig {
                    consumerProguardFiles("consumer-rules.pro")
                }
            }
        }
    }
}