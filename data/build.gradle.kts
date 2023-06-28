plugins {
    id("jp.numero.dagashiapp.buildlogic.primitive.androidlibrary")
    id("jp.numero.dagashiapp.buildlogic.primitive.kotlin")
}

android {
    namespace = "jp.numero.dagashiapp.data"
}

dependencies {
    implementation(projects.model)

    testImplementation("junit:junit:4.13.2")
}