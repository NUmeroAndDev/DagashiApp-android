plugins {
    `kotlin-dsl`
}

group = "jp.numero.dagashiapp.buildlogic"

repositories {
    google()
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(libs.androidGradlePlugin)
    implementation(libs.kotlinPlugin)
    implementation(libs.kotlinSerializationPlugin)
    implementation(libs.hiltPlugin)
    implementation(libs.kspPlugin)
    implementation(libs.baselineProfilePlugin)
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
        register("androidTest") {
            id = "jp.numero.dagashiapp.buildlogic.primitive.androidtest"
            implementationClass = "jp.numero.dagashiapp.buildlogic.primitive.AndroidTestPlugin"
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
        register("appModule") {
            id = "jp.numero.dagashiapp.buildlogic.conventions.appmodule"
            implementationClass = "jp.numero.dagashiapp.buildlogic.conventions.AppModulePlugin"
        }
        register("featureModule") {
            id = "jp.numero.dagashiapp.buildlogic.conventions.featuremodule"
            implementationClass = "jp.numero.dagashiapp.buildlogic.conventions.FeatureModulePlugin"
        }
        register("dataModule") {
            id = "jp.numero.dagashiapp.buildlogic.conventions.datamodule"
            implementationClass = "jp.numero.dagashiapp.buildlogic.conventions.DataModulePlugin"
        }
        register("benchmarkModule") {
            id = "jp.numero.dagashiapp.buildlogic.conventions.benchmarkmodule"
            implementationClass = "jp.numero.dagashiapp.buildlogic.conventions.BenchmarkModulePlugin"
        }
    }
}