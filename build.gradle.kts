plugins {
  `java`
}

group = "dev.codex"
version = "1.0.0"
description = "Brainrot death messages for Minecraft"

java {
  toolchain.languageVersion = JavaLanguageVersion.of(21)
}

repositories {
  mavenCentral()
  maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
  compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
}

tasks {
  compileJava {
    options.release = 21
    options.encoding = "UTF-8"
  }
  jar {
    archiveBaseName.set("BrainrotDeathMessages")
  }
  val pluginVersion = project.version.toString()
  processResources {
    val props = mapOf("version" to pluginVersion)
    inputs.properties(props)
    filesMatching("plugin.yml") {
      expand(props)
    }
  }
}
