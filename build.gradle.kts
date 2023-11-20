// Top-level build file where you can add configuration options common to all sub-projects/modules.
@file:Suppress("DSL_SCOPE_VIOLATION")

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}

// TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.android.application") version "8.1.4" apply false
    ///*alias(libs.plugins.com.android.application) apply false*/
    id("com.android.library") version "8.1.4" apply false
    /*alias(libs.plugins.com.android.library) apply false*/
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    ///*alias(libs.plugins.kotlin.android) apply false*/
    id("com.google.gms.google-services") version "4.3.15" apply false
    ///*alias(libs.plugins.com.google.gms.google.services) apply false*/

    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
}

//task clean(type: Delete) {
//    delete rootProject.buildDir
//}

subprojects {
    afterEvaluate {}
    pluginManager.withPlugin("com.android.library") {
        project.extensions.getByType<com.android.build.gradle.BaseExtension>().apply {
            defaultConfig {
                //buildConfigField("String", "BUILD_DATE", "\"${new Date().format("yyyy/MM/dd HH:mm:ss")}\"")
                //buildConfigField("long", "BUILD_TIME", "${System.currentTimeMillis()}L")
            }
            buildTypes {
                //getByName("debug") {
                //    //isMinifyEnabled = true
                //    ////isDebuggable = true
                //    ////manifestPlaceholders["enableCrashlytics"] = "false"
                //}
                //getByName("release") {
                //    //isMinifyEnabled = true
                //    ////isDebuggable = false
                //    ////manifestPlaceholders["enableCrashlytics"] = "true"
                //}
            }
        }
    }
    pluginManager.withPlugin("com.android.application") {
        project.extensions.getByType<com.android.build.gradle.BaseExtension>().apply {
            defaultConfig {
                //buildConfigField("String", "BUILD_DATE", "\"${Date(System.currentTimeMillis()).format("yyyy/MM/dd HH:mm:ss")}\"")
                //buildConfigField("long", "BUILD_TIME",  "${System.currentTimeMillis()}L")
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
