import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

plugins {
    kotlin("jvm") version "2.0.21"
    id("application")
    id("com.diffplug.spotless") version "6.25.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom("$projectDir/config/detekt.yml")
    baseline = file("$projectDir/config/baseline.xml")
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
    mainClass = "language.klox.KLox"
}

tasks {
    register("format") {
        dependsOn("spotlessApply")
    }

    withType<Detekt>().configureEach {
        dependsOn("spotlessApply")
        jvmTarget = "17"   

        reports {
            html.required.set(true)
            txt.required.set(true)
        }
    }

    withType<DetektCreateBaselineTask>().configureEach {
        jvmTarget = "17"
    }

    test {
        useJUnitPlatform()
    }
}

