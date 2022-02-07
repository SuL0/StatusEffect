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

val pluginStorage = "C:/MC-Development/PluginStorage"
val copyPluginDestination = "C:/MC-Development/마인즈서버/plugins"
dependencies {
    implementation(kotlin("stdlib-jdk8"))
    compileOnly("com.destroystokyo.paper", "paper-api", "1.12.2-R0.1-SNAPSHOT")

    compileOnly(files("$pluginStorage/ServerCore_S.jar"))
}

spigot {
    authors = listOf("SuL")
    apiVersion = "1.12"
    version = project.version.toString()
    softDepends = listOf()
    commands {
        create("statuseffect") {
            permission = "op.op"
        }
    }
}


tasks {
    compileJava.get().options.encoding = "UTF-8"
    compileKotlin.get().kotlinOptions.jvmTarget = "1.8"
    compileTestKotlin.get().kotlinOptions.jvmTarget = "1.8"

//    val copyPlugin = register<Copy>("copyPlugin") {
//        from(files("$pluginStorage/${project.name}_S.jar"))
//        into(file("C:/Users/PHR/Desktop/SERVER2/plugins"))
//    }
    val copyPlugin_2 = register<Copy>("copyPlugin") {
        from(files("$pluginStorage/${project.name}_S.jar"))
        into(file(copyPluginDestination))
    }

    jar {
        archiveFileName.set("${project.name}_S.jar")
        destinationDirectory.set(file(pluginStorage))

        finalizedBy(copyPlugin_2)
    }
}