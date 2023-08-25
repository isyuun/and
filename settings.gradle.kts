pluginManagement {
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
        //카카오 API 관련 maven 추가
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
        //Mapbox 관련 maven 추가
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                // 아래의 username은 항상 `mapbox`로 유지해야 합니다. 개인 사용자 이름이 아닙니다.
                username = "mapbox"
                // gradle.properties에 저장한 비밀 토큰을 비밀번호로 사용합니다.
                password = extra["MAPBOX_DOWNLOADS_TOKEN"].toString()
            }
        }
    }
}
rootProject.name = "and"
//include ":lvl_sample"
//include ":lvl_library"
include(":APP")
include(":APP2")
include(":GPSLogger")
include(":GPS")
include(":Navi")
include(":Navi2")
