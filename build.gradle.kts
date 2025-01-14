/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlin("multiplatform") version "1.5.30" apply false
    kotlin("plugin.serialization") version "1.5.30" apply false
    id("org.jetbrains.kotlin.plugin.noarg") version "1.5.30" apply false
    id("org.jetbrains.dokka") version "1.4.32" apply false
    id("com.github.johnrengelman.shadow") version "7.0.0" apply false
    signing
    `maven-publish`
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30")
        classpath("com.android.tools.build:gradle:4.1.3")
    }
}

rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin> {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension>().apply {
        resolution("@webpack-cli/serve", "1.5.2")
    }
}

subprojects {

    repositories {
        google()
        mavenCentral()
    }

}
