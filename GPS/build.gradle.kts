/*
 *  Copyright 2011 The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 * Copyright (c) 2023. CarePat All right reserved.
 * This software is the proprietary information of CarePet Co.,Ltd.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 8. 25.   description...
 */

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
//plugins {
//    alias(libs.plugins.com.android.library)
//    alias(libs.plugins.kotlin.android)
//}

android {
    namespace = "kr.carepet.gps"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    buildFeatures {
        buildConfig = true
    }
}

plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kotlin.android)
    ///** IY:플랫폼:샘플스:Define the samples to load */
    //id("org.jetbrains.kotlin.jvm") version "1.9.0"
    /** IY:플랫폼:샘플스:Define the samples to load */
    alias(libs.plugins.hilt)
    id("dagger.hilt.android.plugin")
    //id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.kapt")
    ///** IY:플랫폼:샘플스:Define the samples to load */
    //id("com.example.platform")
}
kapt {
    correctErrorTypes = true
}
/**
/** IY:플랫폼:샘플스:Define the samples to load */
dependencies {
    id("com.dropbox.affectedmoduledetector") version "0.2.0"
    id("nl.littlerobots.version-catalog-update") version "0.7.0"
    id("com.github.ben-manes.versions") version "0.44.0"
}
versionCatalogUpdate {
    sortByKey.set(true)
    keep {
        keepUnusedVersions.set(true)
    }
}

affectedModuleDetector {
    baseDir = "${project.rootDir}"
    pathsAffectingAllModules = setOf(
        "gradle/libs.versions.toml",
    )
    excludedModules = setOf<String>()

    logFilename = "output.log"
    logFolder = "${rootProject.buildDir}/affectedModuleDetector"

    val baseRef = findProperty("affected_base_ref") as? String
    // If we have a base ref to diff against, extract the branch name and use it
    if (!baseRef.isNullOrEmpty()) {
        // Remove the prefix from the head.
        // TODO: need to support other types of git refs
        specifiedBranch = baseRef.replace("refs/heads/", "")
        compareFrom = "SpecifiedBranchCommit"
    } else {
        // Otherwise we use the previous commit. This is mostly used for commits to main.
        compareFrom = "PreviousCommit"
    }
}
/** IY:플랫폼:샘플스:Define the samples to load */
*/
dependencies {
    implementation(project(mapOf("path" to ":_APP")))
    implementation(project(mapOf("path" to ":GPL")))

    /** IY:플랫폼:샘플스:Define the samples to load */
    implementation(libs.play.services.location)
    implementation(libs.kotlin.coroutines.play)
    implementation(libs.androidx.work.runtime.ktx)
    //implementation(project(":samples:base"))
    implementation(platform(libs.compose.bom))
    androidTestImplementation(platform(libs.compose.bom))
    implementation(libs.casa.base)
    ksp(libs.casa.processor)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.core)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.foundation.foundation)
    implementation(libs.compose.runtime.runtime)
    implementation(libs.compose.runtime.livedata)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.compose.ui.ui)
    implementation(libs.compose.material3)
    implementation(libs.coil.compose)
    implementation(libs.coil.video)
    implementation(libs.accompanist.permissions)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
    //androidTestImplementation(libs.androidx.test.core)
    //androidTestImplementation(libs.androidx.test.runner)
    implementation(libs.androidx.exifinterface)
    /** IY:플랫폼:샘플스:Define the samples to load */

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}