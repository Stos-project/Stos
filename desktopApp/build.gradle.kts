import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

val versionProperties = Properties().apply {
    load(rootProject.file("version.properties").inputStream())
}

val versionMajor = versionProperties["versionMajor"].toString().toInt()
val versionMinor = versionProperties["versionMinor"].toString().toInt()
val versionPatch = versionProperties["versionPatch"].toString().toInt()

dependencies {
    implementation(project(":shared"))

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.compose.uiToolingPreview)

    implementation(libs.slf4j)
}

kotlin {
    jvmToolchain(21)
}

compose.desktop {
    application {
        mainClass = "com.m4ykey.stos.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.m4ykey.stos"
            packageVersion = "$versionMajor.$versionMinor.$versionPatch"
            vendor = "m4ykey"
            description = "Unofficial StackExchange client"
            copyright = "2026 m4ykey"

            windows {
                menuGroup = "Stos"
                shortcut = true
                perUserInstall = true
                dirChooser = true
                iconFile.set(project.file("icons/stos.ico"))
                upgradeUuid = "68f80ac2-8160-4405-84cf-ddabc6603ffc"
            }
        }
    }
}