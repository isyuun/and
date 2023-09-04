pluginManagement {
    //includeBuild("build-logic")     ///** IY:플랫폼:샘플스:Define the samples to load */
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
/**
/** IY:플랫폼:샘플스:Define the samples to load */
var samples = emptyList<String>()

/** If the local.properties define specific samples use those only*/
val propertiesFile = file("local.properties")
if (propertiesFile.exists()) {
val properties = java.util.Properties()
properties.load(propertiesFile.inputStream())
if (properties.containsKey("target.samples")) {
// Specify the sample module name (e.g :samples:privacy:permissions) or comma separated ones
samples = listOf(":samples:base") + properties["target.samples"].toString().split(",")
}
}

/** Dynamically include samples under /app-catalog/samples/ folder if no target.samples were defined*/
if (samples.isEmpty()) {
samples = buildList {
val separator = File.separator
// Find all build.gradle files under samples folder
settingsDir.walk()
.filter { it.name == "build.gradle" || it.name == "build.gradle.kts" }
.filter { it.path.contains("${separator}samples${separator}") }
.map { it.parent.substring(rootDir.path.length) }
.forEach {
add(it.replace(separator, ":"))
}
}
}

/** include all available samples and store it in :app project extras.*/
println("Included samples: $samples")
include(*samples.toTypedArray())
gradle.beforeProject {
if (name == "app") {
extra["samples"] = samples
}
}
/** IY:플랫폼:샘플스:Define the samples to load */
 */
rootProject.name = "and"
//include ":lvl_sample"
//include ":lvl_library"
//include(":complete")
include(":_APP")
include(":APP")
include(":APP2")
include(":GPL")
//include(":GPS")
//include(":Navi")
//include(":Navi2")
