package jp.numero.dagashiapp.buildlogic.conventions

import androidx.baselineprofile.gradle.producer.BaselineProfileProducerExtension
import com.android.build.api.dsl.ManagedVirtualDevice
import jp.numero.dagashiapp.buildlogic.primitive.test
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke

class BenchmarkModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("jp.numero.dagashiapp.buildlogic.primitive.androidtest")
                apply("jp.numero.dagashiapp.buildlogic.primitive.kotlin")
                apply("androidx.baselineprofile")
            }
            val deviceName = "pixel2Api31"
            test {
                testOptions {
                    managedDevices {
                        devices {
                            maybeCreate(deviceName, ManagedVirtualDevice::class.java).apply {
                                device = "Pixel 2"
                                apiLevel = 31
                                systemImageSource = "aosp"
                            }
                        }
                    }
                }
            }
            extensions.configure<BaselineProfileProducerExtension> {
                managedDevices += deviceName
                useConnectedDevices = false
            }
        }
    }
}