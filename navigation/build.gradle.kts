plugins {
    id("jp.numero.dagashiapp.buildlogic.primitive.androidlibrary")
    id("jp.numero.dagashiapp.buildlogic.primitive.kotlin")
    id("jp.numero.dagashiapp.buildlogic.primitive.compose")
    id("jp.numero.dagashiapp.buildlogic.primitive.ksp")
}

android {
    namespace = "jp.numero.dagashiapp.navigation"
}

ksp {
    arg("compose-destinations.generateNavGraphs", "false")
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.compose.ui)

    implementation(libs.composeDestinations.core)
    ksp(libs.composeDestinations.ksp)

    implementation(libs.licenses)

    testImplementation("junit:junit:4.13.2")
}