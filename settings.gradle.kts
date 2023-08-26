pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "DagashiApp"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":appwidget")
include(":ui")
include(":data")
include(":model")
include(":data-impl")
include(":feature:milestones")
include(":feature:settings")
include(":feature:licenses")
include(":benchmark")
