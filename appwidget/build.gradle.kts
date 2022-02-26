import jp.numero.dagashiapp.buildlogic.primitive.libs

plugins {
    id("jp.numero.dagashiapp.buildlogic.primitive.androidlibrary")
    id("jp.numero.dagashiapp.buildlogic.primitive.kotlin")
    id("jp.numero.dagashiapp.buildlogic.primitive.hilt")
}

android {
    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = libs.findVersion("composeCompiler").get().toString()
    }
}

dependencies {
    implementation(projects.model)
    implementation(projects.data)

    testImplementation("junit:junit:4.13.2")
}