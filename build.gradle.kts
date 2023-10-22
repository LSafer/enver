plugins {
    kotlin("multiplatform") version "1.9.0"
    id("maven-publish")
}

group = "net.lsafer"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        withJava()
    }
    js(IR) {
        browser {
            binaries.executable()

            testTask(Action {
                useMocha {
                    timeout = "${90_000}"
                }

                nodeJsArgs += "--expose_gc"
            })
        }
        nodejs {
            binaries.executable()

            testTask(Action {
                useMocha {
                    timeout = "${90_000}"
                }

                nodeJsArgs += "--expose_gc"
            })
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(kotlin("reflect"))
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(platform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:1.0.0-pre.597"))
                implementation("org.jetbrains.kotlin-wrappers:kotlin-js")
            }
        }
    }
}

// https://github.com/jitpack/jitpack.io/issues/3853#issuecomment-1683838845
rootProject.plugins.withType(org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin::class.java) {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension>().nodeVersion = "16.0.0"
}

//
//afterEvaluate {
//    publishing {
//        publications {
//            create<MavenPublication>("maven") {
//                from(components["java"])
//                artifactId = "enver"
//            }
//        }
//    }
//}
//
