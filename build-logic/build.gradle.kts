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

dependencies {
    implementation(libs.androidGradlePlugin)
    implementation(libs.kotlinPlugin)
    implementation(libs.kotlinSerializationPlugin)
    implementation(libs.hiltPlugin)
    implementation(libs.kspPlugin)
}

gradlePlugin {
    plugins {
        // TODO: plugin
    }
}