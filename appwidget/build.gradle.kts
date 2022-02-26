plugins {
    id("jp.numero.dagashiapp.buildlogic.primitive.androidlibrary")
    id("jp.numero.dagashiapp.buildlogic.primitive.kotlin")
    id("jp.numero.dagashiapp.buildlogic.primitive.hilt")
    id("jp.numero.dagashiapp.buildlogic.primitive.compose")
}

dependencies {
    implementation projects.model
    implementation projects.data

    testImplementation 'junit:junit:4.13.2'
}