plugins {
  kotlin("jvm") version "1.9.20"
}

sourceSets {
  main {
    kotlin.srcDir("src")
  }
}

dependencies {
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")
  implementation(kotlin("reflect"))
}

tasks {
  wrapper {
    gradleVersion = "8.5"
  }
}
