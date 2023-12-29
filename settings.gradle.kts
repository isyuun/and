pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    //repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://devrepo.kakao.com/nexus/content/groups/public/")
        maven("https://naver.jfrog.io/artifactory/maven/")
    }
}

rootProject.name = "and"
include(":TEST")
//include ":lvl_sample"
//include ":lvl_library"
include(":_APP")
include(":APP")
include(":NAV")
include(":GPS")
include(":GPX")
include(":MAP")
include(":Navi")
