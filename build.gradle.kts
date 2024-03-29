import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin ("jvm") version "1.6.10"
  application
  id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.jiangkedev"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.3.4"
val junitJupiterVersion = "5.7.0"

val mainVerticleName = "com.jiangkedev.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-web-validation")
  implementation("io.vertx:vertx-web")
  //mysql依赖
  implementation("io.vertx:vertx-mysql-client")
  implementation("io.vertx:vertx-lang-kotlin")
  //jackson依赖
  implementation("com.fasterxml.jackson.core:jackson-databind")
  implementation(kotlin("stdlib-jdk8"))
  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "1.8"

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

//https://stackoverflow.com/questions/73012633/vert-x-address-already-in-use-bind-not-kill-processes-windows
//必要要移除"--redeploy=$watchForChange",否则会导致IDEA无法正常结束进程,并且会导致无法正常调试
tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
