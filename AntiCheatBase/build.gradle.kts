plugins {
    java
}

group = "me.novocx"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api("com.github.retrooper:packetevents:v1.8-pre-16")
    api(project(":API"))
}

tasks {
    shadowJar {
        relocate("io.github.retrooper", "me.novocx")
    }
}
