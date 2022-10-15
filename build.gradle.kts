plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("xyz.jpenilla.run-paper") version "1.0.6"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("io.papermc.paperweight.userdev") version "1.3.5"
}

group = "com.stifflered"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    paperDevBundle("1.19.2-R0.1-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
}

bukkit {
    main = "com.stifflered.chatrestrict.ChatRestrictPlugin"
    name = rootProject.name
    apiVersion = "1.18"
    version = "1.0.0"

}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    runServer {
        minecraftVersion("1.19.2")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}