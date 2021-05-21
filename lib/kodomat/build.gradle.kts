/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.plugin.noarg")
    id("org.jetbrains.dokka")
    signing
    `maven-publish`
}

group = "hu.simplexion.zakadabar"
version = "2021.5.18"

noArg {
    annotation("kotlinx.serialization.Serializable")
}

kotlin {

    jvm { }

    js {
        browser()
    }

    sourceSets["commonMain"].dependencies {
        implementation(project(":core"))
        implementation(project(":lib:markdown"))
    }

}