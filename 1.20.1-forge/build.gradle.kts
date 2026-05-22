plugins {
    id("legacyforge-mod-conventions")
    id("legacyforge-config-conventions")
}

val configuredVersion: String by project

// Mod Dependencies
dependencies {
    modRuntimeOnly(libs.configured) { version { require(configuredVersion) } }
    modImplementation("curse.maven:applied-energistics-2-223794:7148487")
}
