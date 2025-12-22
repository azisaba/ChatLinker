plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.shadow)
}

group = "net.azisaba"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.kord.dev/snapshots")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(libs.jda) {
        exclude(module = "opus-java")
        exclude(module = "tink")
    }
    implementation(libs.bundles.exposed)
    implementation(libs.mariadbJavaClient)
    implementation(libs.guava)
    implementation(libs.slf4jSimple)
}

kotlin {
    jvmToolchain(21)
}

tasks {
    test {
        useJUnitPlatform()
    }

    shadowJar {
        minimize()
    }
}
