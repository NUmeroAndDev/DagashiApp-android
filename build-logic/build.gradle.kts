plugins {
    `kotlin-dsl`
}

group = "jp.numero.dagashiapp.buildlogic"

repositories {
    google()
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {
    implementation(libs.androidGradlePlugin)
    implementation(libs.kotlinPlugin)
    implementation(libs.kotlinSerializationPlugin)
    implementation(libs.hiltPlugin)
    implementation(libs.kspPlugin)
}

gradlePlugin {
    plugins {
        // primitives
        register("androidApplication") {
            id = "jp.numero.dagashiapp.buildlogic.primitive.androidapplication"
            implementationClass =
                "jp.numero.dagashiapp.buildlogic.primitive.AndroidApplicationPlugin"
        }
        register("androidLibrary") {
            id = "jp.numero.dagashiapp.buildlogic.primitive.androidlibrary"
            implementationClass = "jp.numero.dagashiapp.buildlogic.primitive.AndroidLibraryPlugin"
        }
        register("compose") {
            id = "jp.numero.dagashiapp.buildlogic.primitive.compose"
            implementationClass = "jp.numero.dagashiapp.buildlogic.primitive.ComposePlugin"
        }
        register("hilt") {
            id = "jp.numero.dagashiapp.buildlogic.primitive.hilt"
            implementationClass = "jp.numero.dagashiapp.buildlogic.primitive.HiltPlugin"
        }
        register("kotlin") {
            id = "jp.numero.dagashiapp.buildlogic.primitive.kotlin"
            implementationClass = "jp.numero.dagashiapp.buildlogic.primitive.KotlinPlugin"
        }
        register("ksp") {
            id = "jp.numero.dagashiapp.buildlogic.primitive.ksp"
            implementationClass = "jp.numero.dagashiapp.buildlogic.primitive.KspPlugin"
        }

        // conventions
        register("featureModule") {
            id = "jp.numero.dagashiapp.buildlogic.conventions.featuremodule"
            implementationClass = "jp.numero.dagashiapp.buildlogic.conventions.FeatureModulePlugin"
        }
    }
}