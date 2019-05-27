import org.gradle.api.JavaVersion

object Plugins {
    const val androidLibrary = "com.android.library"
    const val kotlinAndroid = "kotlin-android"
    const val gradleVersions = "com.github.ben-manes.versions"
}

object Versions {
    const val kotlin = "1.3.31"
    const val androidGradlePlugin = "3.4.1"
    const val gradleVersions = "0.21.0"

    const val minSdk = 21
    const val targetSdk = 28

    val sourceCompatibility = JavaVersion.VERSION_1_8
    val targetCompatibility = JavaVersion.VERSION_1_8

    const val coreKtx = "1.0.2"
}

object BuildTools {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val gradleVersions = "com.github.ben-manes:gradle-versions-plugin:${Versions.gradleVersions}"
}

object Libs {
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
}
