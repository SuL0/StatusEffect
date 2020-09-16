import kr.entree.spigradle.kotlin.*

plugins {
    kotlin("jvm") version "1.3.72"
    id("kr.entree.spigradle") version "2.2.3"
}


group = "kr.sul"
version = "1.0-SNAPSHOT"

tasks.compileJava.get().options.encoding = "UTF-8"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    mavenLocal()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly("com.destroystokyo.paper", "paper-api", "1.12.2-R0.1-SNAPSHOT")
}


spigot {
    authors = listOf("SuL")
    version = "1.12"
}


val shade = configurations.create("shade")
shade.extendsFrom(configurations.implementation.get())

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    jar {
        from(
                shade.map {
                    if (it.isDirectory)
                        it
                    else
                        zipTree(it)
                }
        )
    }
}