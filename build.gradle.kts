// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("io.github.gradle-nexus:publish-plugin:1.1.0")
    }
}

apply(plugin = "io.github.gradle-nexus.publish-plugin")
apply(from = "${rootDir}/scripts/publish-root.gradle")

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}