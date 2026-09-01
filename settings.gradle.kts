@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
include(
    ":app",
    ":data:model",
    ":data:repository",
    ":data:source:local",
    ":data:source:localImpl",
    ":data:source:remote",
    ":domain",
    ":presentation:shared",
    ":presentation:about",
    ":presentation:collections",
    ":presentation:collectionDetails",
    ":presentation:wallpaperDetails",
    ":presentation:utilities",
    ":presentation:debugMenu"
)
rootProject.name = "ChignonMignonWallpapers"
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io/") }
    }
}