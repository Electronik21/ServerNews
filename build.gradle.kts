plugins {
  java
  id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "to.us.electr0n.servernews"

java {
  sourceCompatibility = JavaVersion.VERSION_21
  targetCompatibility = JavaVersion.VERSION_21
}

repositories {
  mavenCentral()
  maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
  compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

tasks {
  runServer {
    minecraftVersion("1.21.4")
  }
}
