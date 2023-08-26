plugins {
    id("jp.numero.dagashiapp.buildlogic.conventions.appmodule")
    id("com.google.android.gms.oss-licenses-plugin")
}

android {
    namespace = "jp.numero.dagashiapp"

    defaultConfig {
        applicationId = "jp.numero.dagashiapp"
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("benchmark") {
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks.add("release")
            isDebuggable = false
        }
    }

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    implementation(projects.feature.milestones)
    implementation(projects.feature.settings)
    implementation(projects.feature.licenses)
    implementation(projects.dataImpl)
    implementation(projects.appwidget)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.core)
    implementation(libs.androidx.coreSplash)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.window)

    implementation(libs.androidx.profileinstaller)
}