plugins {
    id("jp.numero.dagashiapp.buildlogic.primitive.androidlibrary")
    id("jp.numero.dagashiapp.buildlogic.primitive.kotlin")
    id("jp.numero.dagashiapp.buildlogic.primitive.hilt")
}

android {
    namespace = "jp.numero.dagashiapp.repository"
}

dependencies {
    implementation(projects.model)
    implementation(projects.dataImpl)

    testImplementation("junit:junit:4.13.2")
}