plugins {
    id("jp.numero.dagashiapp.buildlogic.conventions.datamodule")
    id("kotlinx-serialization")
}

android {
    namespace = "jp.numero.dagashiapp.data"
}

dependencies {
    implementation(libs.kotlinx.serialization)

    implementation(libs.androidx.datastore.preference)

    implementation(libs.okhttp3.core)
    debugImplementation(libs.okhttp3.loggingInterceptor)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter)

    testImplementation("junit:junit:4.13.2")
}