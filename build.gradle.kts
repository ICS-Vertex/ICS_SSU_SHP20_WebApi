
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
}

group = "shp.ssu.icsvertex.nl"
version = "0.0.1"

application {
    mainClass.set("shp.ssu.icsvertex.nl.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}


dependencies {
    implementation("nl.icsvertex.ktor:ics_core_server:0.0.0.3")
    implementation("nl.icsvertex.ktor:ics_core_exposed:0.0.0.1")
    implementation("nl.icsvertex.ssu.shp:ssu_shp_core-jvm:0.0.0.4")
}
