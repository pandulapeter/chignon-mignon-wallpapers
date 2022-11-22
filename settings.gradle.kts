include(
    ":app",
    ":data:model",
    ":data:repository",
    ":data:source:local",
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
rootProject.name = "Chignon Mignon Wallpapers"
enableFeaturePreview("VERSION_CATALOGS")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io/") }
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }
}