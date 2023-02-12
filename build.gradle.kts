import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension
import org.jetbrains.kotlin.noarg.gradle.NoArgExtension


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
val kotlinVersion = "1.6.10"
val junitJupiterVersion = "5.7.0"

//val mainVerticleName = "com.jiangkedev.MainVerticle"
val mainVerticleName = "com.jiangkedev.vertex.PgVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

val mutinyVertxVersion="2.14.2"

application {
  mainClass.set(launcherClassName)
}

//仓库设置
repositories{
  mavenLocal()
  maven() { url= uri("https://maven.aliyun.com/nexus/content/groups/public/") }
  mavenCentral()
}

//依赖设置
dependencies {
  implementation("io.smallrye.reactive:smallrye-mutiny-vertx-core:${mutinyVertxVersion}")
  implementation("io.smallrye.reactive:smallrye-mutiny-vertx-web:${mutinyVertxVersion}")
  //pg客户端
  implementation("io.smallrye.reactive:smallrye-mutiny-vertx-pg-client:${mutinyVertxVersion}")
  //hibernate依赖
  implementation("org.hibernate.reactive:hibernate-reactive-core:1.0.3.Final")
  //guava依赖
  //版本31有兼容性报错,参考:https://github.com/smallrye/jandex/issues/147
  implementation("com.google.guava:guava:30.1.1-jre")
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  //mysql驱动
  implementation("io.vertx:vertx-mysql-client")

  //日志组件
  implementation("ch.qos.logback:logback-classic:1.4.5")
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

buildscript {
  dependencies {
    //添加无参方法插件
    classpath("org.jetbrains.kotlin:kotlin-noarg:1.6.10")
    //全开插件
    classpath("org.jetbrains.kotlin:kotlin-allopen:1.6.10")
  }
}

apply{
  plugin("kotlin-jpa")
  plugin("kotlin-allopen")
}

//全开插件配置
configure<AllOpenExtension> {
  annotation("javax.persistence.Entity")
}

//无参扩展函数
configure<NoArgExtension> {
  annotation("javax.persistence.Entity")
}
