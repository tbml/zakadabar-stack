/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import zakadabar.gradle.Versions

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.plugin.noarg")
}

group = "hu.simplexion.zakadabar"
version = Versions.zakadabar

noArg {
    annotation("kotlinx.serialization.Serializable")
}

kotlin {

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    js {
        nodejs()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:core"))
                implementation(project(":lib:blobs"))
                implementation(project(":lib:schedule"))
                implementation(project(":lib:email"))
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(kotlin("test-junit"))
            }
        }
    }

    sourceSets["jvmMain"].dependencies {
        dependencies {
            implementation("io.ktor:ktor-server-netty:${Versions.ktor}")
            implementation("com.h2database:h2:${Versions.h2}")
            implementation("org.xerial:sqlite-jdbc:${Versions.sqliteJdbc}")
        }
    }

}