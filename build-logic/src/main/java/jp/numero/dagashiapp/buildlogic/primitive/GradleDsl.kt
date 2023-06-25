package jp.numero.dagashiapp.buildlogic.primitive

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

fun Project.application(action: BaseAppModuleExtension.() -> Unit) {
    extensions.configure(action)
}

fun Project.library(action: LibraryExtension.() -> Unit) {
    extensions.configure(action)
}

fun Project.android(action: BaseExtension.() -> Unit) {
    extensions.configure(action)
}

fun Project.commonConfiguration() {
    android {
        namespace?.let {
            this.namespace = it
        }
        compileSdkVersion(33)

        defaultConfig {
            minSdk = 23
            targetSdk = 33
        }

        compileOptions {
            isCoreLibraryDesugaringEnabled = true
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        dependencies {
            add("coreLibraryDesugaring", libs.findLibrary("desugarJdkLibs").get())
        }
    }
}
