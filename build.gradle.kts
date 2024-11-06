plugins {
    kotlin("jvm") version "2.0.21"
    id("application")
    id("com.diffplug.spotless") version "6.25.0"
}

spotless {
    kotlin {
        target("src/**/*.kt")
        ktfmt().kotlinlangStyle()
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("com.willowtreeapps.assertk:assertk:0.28.1")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass = "klox.KLox"
}

tasks {
    register("format") {
        dependsOn("spotlessApply")
    }

    test {
        useJUnitPlatform()
    }
}

