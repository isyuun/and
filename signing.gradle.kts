/**
 * gradle kotlin-dsl move android {} into subproject {} in root project build.gradle.kts
 * @see <a href="https://stackoverflow.com/questions/58255544/gradle-kotlin-dsl-move-android-into-subproject-in-root-project-build-gradl">gradle kotlin-dsl move android {} into subproject {} in root project build.gradle.kts</a>
 */
subprojects {
    afterEvaluate {}
    pluginManager.withPlugin("com.android.library") {
        project.extensions.getByType<com.android.build.gradle.BaseExtension>().apply {
            defaultConfig {
                //ext.BUILD_DATE = { return new Date().format("yyyy/MM/dd HH:mm:ss") }
                //ext.BUILD_TIME = { return System.currentTimeMillis() + "L" }
                //buildConfigField("String", "BUILD_DATE", "\"" + new Date().format("yyyy/MM/dd HH:mm:ss") + "\"")
                //buildConfigField("long", "BUILD_TIME", System.currentTimeMillis() + "L")
                //consumerProguardFiles "consumer-rules.pro"
            }
            buildTypes {
                getByName("debug") {
                    //isMinifyEnabled = true
                    ////isDebuggable = true
                    ////manifestPlaceholders["enableCrashlytics"] = "false"
                }
                getByName("release") {
                    //isMinifyEnabled = true
                    ////isDebuggable = false
                    ////manifestPlaceholders["enableCrashlytics"] = "true"
                }
            }
        }
    }
    pluginManager.withPlugin("com.android.application") {
        project.extensions.getByType<com.android.build.gradle.BaseExtension>().apply {
            defaultConfig {
                //ext.BUILD_DATE = { Date().format("yyyy/MM/dd HH:mm:ss") }
                //ext.BUILD_TIME = { System.currentTimeMillis() + "L" }
                //buildConfigField("String", "BUILD_DATE", "\"" + new Date().format("yyyy/MM/dd HH:mm:ss") + "\"")
                //buildConfigField("long", "BUILD_TIME", System.currentTimeMillis() + "L")
            }
            signingConfigs {
                getByName("debug") {
                    storeFile = file("../.APK/kr.carepet.debug.jks")
                }
                create("release") {
                    storeFile = file("../.APK/kr.carepet.release.jks")
                    storePassword = "is230710!!"
                    keyAlias = "carepet"
                    keyPassword = "is230710!!"
                }
            }
            buildTypes {
                getByName("debug") {
                    signingConfig = signingConfigs.getByName("debug")
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro", "retrofit2.pro", "proguard.cfg")
                    isMinifyEnabled = true
                    //isDebuggable = true
                    //manifestPlaceholders["enableCrashlytics"] = "false"
                }
                getByName("release") {
                    signingConfig = signingConfigs.getByName("release")
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro", "retrofit2.pro", "proguard.cfg")
                    isMinifyEnabled = true
                    //isDebuggable = false
                    //manifestPlaceholders["enableCrashlytics"] = "true"
                }
            }
        }
    }
    pluginManager.withPlugin("kotlin-android") {
        //project.extensions.getByType<com.android.build.gradle.BaseExtension>().apply {
        //    // To inline the bytecode built with JVM target 1.8 into
        //    // bytecode that is being built with JVM target 1.6. (e.g. navArgs)
        //    compileOptions {
        //        sourceCompatibility JavaVersion.VERSION_1_8
        //                targetCompatibility JavaVersion.VERSION_1_8
        //    }
        //    kotlinOptions {
        //        jvmTarget = "1.8"
        //    }
        //}
    }
}
