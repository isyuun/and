// Top-level build file where you can add configuration options common to all sub-projects/modules.
@file:Suppress("DSL_SCOPE_VIOLATION")

// TODO: Remove once KTIJ-19369 is fixed
plugins {
    //id("com.android.application") version "8.1.0" apply false
    alias(libs.plugins.com.android.application) apply false
    //id("com.android.library") version "8.1.0" apply false
    alias(libs.plugins.com.android.library) apply false
    //id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    alias(libs.plugins.kotlin.android) apply false
    //id("com.google.gms.google-services") version "4.3.15" apply false
    alias(libs.plugins.com.google.gms.google.services) apply false
    //
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    //
    alias(libs.plugins.affectedmoduledetector)
    alias(libs.plugins.versionCatalogUpdate)
    alias(libs.plugins.benManesVersions)

    //id("com.example.platform")      ///** IY:플랫폼:샘플스:Define the samples to load */
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(gradleKotlinDsl())
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}
/**
/** IY:플랫폼:샘플스:Define the samples to load */
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