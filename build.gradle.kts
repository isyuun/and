// Top-level build file where you can add configuration options common to all sub-projects/modules.
@file:Suppress("DSL_SCOPE_VIOLATION")

buildscript {
    dependencies {
        classpath(libs.google.services)
    }
}

// TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.android.application") version "8.2.0" apply false
    ///*alias(libs.plugins.com.android.application) apply false*/
    id("com.android.library") version "8.2.0" apply false
    /*alias(libs.plugins.com.android.library) apply false*/
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    ///*alias(libs.plugins.kotlin.android) apply false*/
    id("com.google.gms.google-services") version "4.3.15" apply false
    ///*alias(libs.plugins.com.google.gms.google.services) apply false*/

    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false

    //id("org.jetbrains.kotlin.plugin.serialization") version "1.6.0" apply false
}

/**
 * gradle kotlin-dsl move android {} into subproject {} in root project build.gradle.kts
 * @see <a href="https://stackoverflow.com/questions/58255544/gradle-kotlin-dsl-move-android-into-subproject-in-root-project-build-gradl">gradle kotlin-dsl move android {} into subproject {} in root project build.gradle.kts</a>
 */
subprojects {
    afterEvaluate {}
    pluginManager.withPlugin("com.android.library") {
        project.extensions.getByType<com.android.build.gradle.BaseExtension>().apply {
            defaultConfig {
                resValue("string", "build_time", "${System.currentTimeMillis()}")
            }
        }
    }
    pluginManager.withPlugin("com.android.application") {
        project.extensions.getByType<com.android.build.gradle.BaseExtension>().apply {
            defaultConfig {
                resValue("string", "build_time", "${System.currentTimeMillis()}")
            }
            signingConfigs {
                getByName("debug") {
                    storeFile = file("../.APK/net.pettip.debug.jks")
                }
                create("release") {
                    storeFile = file("../.APK/net.pettip.release.jks")
                    storePassword = "is230710!!"
                    keyAlias = "carepet"
                    keyPassword = "is230710!!"
                }
            }
            buildTypes {
                getByName("debug") {
                    signingConfig = signingConfigs.getByName("debug")
                    proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro", "retrofit2.pro", "proguard.cfg")
                    //isMinifyEnabled = true
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
