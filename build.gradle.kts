buildscript {
    dependencies {
        classpath("com.google.android.gms:oss-licenses-plugin:0.10.6")
        // ref: https://github.com/google/dagger/issues/3068
        classpath("com.squareup:javapoet:1.13.0")
    }
}

plugins {
    alias(libs.plugins.androidGradlePlugin) apply false
    alias(libs.plugins.kotlinPlugin) apply false
    alias(libs.plugins.kotlinSerializationPlugin) apply false
    alias(libs.plugins.kspPlugin) apply false
}

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>()
        .configureEach {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_11.toString()
                compilerOptions
                    .freeCompilerArgs
                    .add("-Xcontext-receivers")
            }
        }
}
