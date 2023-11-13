// This is the top-level Gradle settings for the project.
// The module dependencies it contains may be symbolic links to peer folders.
//
// This file is generated by the Skip transpiler plugin and is
// derived from the aggregate Skip/skip.yml files from the SPM project.
// Edits made directly to this file will be lost.
//
// Open with External Editor to build and run this project in an IDE.
//
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
    }
}

rootProject.name = "skip.ui"
include(":SkipUI")
include(":SkipFoundation")
include(":SkipLib")
include(":SkipUnit")
include(":SkipModel")
