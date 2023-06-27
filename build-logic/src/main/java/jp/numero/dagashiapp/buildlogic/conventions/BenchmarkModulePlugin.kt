package jp.numero.dagashiapp.buildlogic.conventions

import jp.numero.dagashiapp.buildlogic.primitive.test
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import com.android.build.api.dsl.ManagedVirtualDevice

class BenchmarkModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("jp.numero.dagashiapp.buildlogic.primitive.androidtest")
                apply("jp.numero.dagashiapp.buildlogic.primitive.kotlin")
            }
            test {
                experimentalProperties["android.experimental.self-instrumenting"] = true
                testOptions {
                    managedDevices {
                        devices {
                            maybeCreate("pixel2Api31", ManagedVirtualDevice::class.java).apply {
                                device = "Pixel 2"
                                apiLevel = 31
                                systemImageSource = "aosp"
                            }
                        }
                    }
                }
            }
        }
    }
}