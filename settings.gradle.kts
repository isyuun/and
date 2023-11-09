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
        //카카오 API 관련 maven 추가
        maven("https://devrepo.kakao.com/nexus/content/groups/public/")
        maven("https://naver.jfrog.io/artifactory/maven/")
    }
}

rootProject.name = "and"
//include ":lvl_sample"
//include ":lvl_library"
//include(":TEST")
include(":_APP")
include(":APP")
include(":NAV")
//include(":GPL")
include(":GPS")
include(":GPX")
include(":MAP")
include(":Navi")
