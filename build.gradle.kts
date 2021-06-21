plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "me.novocx"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

}

val leadVersion: String = version.toString()
val leadGroup: String = group.toString()

subprojects {
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "java-library")

    repositories {
        maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        maven(url = "https://nexus.funkemunky.cc/content/repositories/releases/")
        maven(url = "https://jitpack.io")
    }

    dependencies {
        // spigot
        compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

        // lombok
        implementation("org.projectlombok:lombok:1.18.20")
        annotationProcessor("org.projectlombok:lombok:1.18.20")

        // asm
        implementation("cc.funkemunky.utils:asm-all:5.2")

        implementation("org.jetbrains:annotations:20.1.0")
    }

    java.sourceCompatibility = JavaVersion.VERSION_1_8
    java.targetCompatibility = JavaVersion.VERSION_1_8

    tasks.withType<JavaCompile> {
        val compilerArgs = options.compilerArgs
        compilerArgs.add("-XDignore.symbol.file")
    }

    version = leadVersion
    group = leadGroup
}
