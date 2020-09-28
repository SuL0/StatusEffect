plugins {
    kotlin("jvm") version "1.4.10"
    id("kr.entree.spigradle") version "2.2.3"
}

group = "kr.sul"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    mavenLocal()
}

dependencies {
    compileOnly(files("C:/Users/PHR/Desktop/PluginStorage/KotlinLoader_S-1.4.10.jar"))
    compileOnly("com.destroystokyo.paper", "paper-api", "1.12.2-R0.1-SNAPSHOT")
}

spigot {
    authors = listOf("SuL")
    apiVersion = "1.12"
    version = project.version.toString()
}


tasks.compileJava.get().options.encoding = "UTF-8"

tasks {
    compileKotlin.get().kotlinOptions.jvmTarget = "1.8"
    compileTestKotlin.get().kotlinOptions.jvmTarget = "1.8"

    jar {
        archiveFileName.set("${project.name}_S.jar")
        destinationDirectory.set(file("C:/Users/PHR/Desktop/PluginStorage"))
    }
}

