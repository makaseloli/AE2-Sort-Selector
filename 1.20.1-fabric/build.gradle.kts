plugins {
    id("fabric-loom-remap-mod-conventions")
    id("fabric-config-conventions")
}

val forgeconfigscreensVersion: String by project
val modmenuVersion: String by project

// Mod Dependencies
dependencies {
    modRuntimeOnly(libs.forgeconfigscreens) { version { require(forgeconfigscreensVersion) } }
    modRuntimeOnly(libs.modmenu) { version { require(modmenuVersion) } }
    modImplementation("curse.maven:applied-energistics-2-223794:7148494")
}
