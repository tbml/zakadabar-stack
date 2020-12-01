/*
 * Copyright © 2020, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka")
    id("com.github.johnrengelman.shadow")
    application
    signing
    `maven-publish`
}

group = "hu.simplexion.zakadabar"
version = "2020.10.26-SNAPSHOT"

application {
    mainClassName = "zakadabar.stack.backend.app.ServerKt"
}

kotlin {

    jvm {
        withJava()
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    js {
        browser()
    }

    sourceSets["commonMain"].dependencies {
        implementation(project(":core"))
    }
}