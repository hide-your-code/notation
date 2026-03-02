pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "notation"
include(":app")
include(":domain")
include(":data")

include(":core:common")
include(":core:model")

include(":features:modifyNote:api")
include(":features:modifyNote:impl")

include(":features:notesPaging:api")
include(":features:notesPaging:impl")

include(":features:notesManual:api")
include(":features:notesManual:impl")
include(":features:welcome")
include(":features:welcome:api")
include(":features:welcome:impl")
