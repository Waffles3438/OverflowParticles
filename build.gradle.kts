import net.ornithemc.ploceus.api.PloceusGradleExtensionApi
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("dev.kikugie.loom-back-compat")
    id("org.jetbrains.kotlin.jvm") version "2.4.10"
    id("net.fabricmc.fabric-loom-remap") version "1.17-SNAPSHOT" apply false
    id("ploceus") version "1.17.4" apply false
}

val isOrnithe = stonecutter.current.version == "1.8.9"
val ploceus = if (isOrnithe) {
    pluginManager.apply("net.fabricmc.fabric-loom-remap")
    pluginManager.apply("ploceus")
    configurations.configureEach { exclude(group = "org.lwjgl.lwjgl") }
    extensions.getByType<PloceusGradleExtensionApi>().apply { setIntermediaryGeneration(2) }
} else null

val modid: String = sc.properties["mod.id"]
val modversion: String = sc.properties["mod.version"]
val mcversion = sc.current.version
val oneconfigversion: String = sc.properties["deps.oneconfig"]
val loaderversion: String = sc.properties["deps.fabric_loader"]
val upstreamversion: String = sc.properties["deps.overflowparticles"]
val loader = if (isOrnithe) "ornithe" else "fabric"
val requiredJava = if (isOrnithe || sc.current.parsed >= "26.1") 25 else 21

group = sc.properties.get<String>("mod.group")
version = "$modversion+$mcversion"
base.archivesName = modid

stonecutter {
    constants["fabric"] = true
    constants["forge"] = false
    constants["neoforge"] = false
    constants["forge_like"] = false
}

// Keep the former fork available for reference, but never compile or ship its classes.
// Stonecutter's public file processor lets the companion use an isolated source tree.
fun companionSources(sourceSet: String) = tasks.register("generateCompanion${sourceSet.replaceFirstChar { it.uppercase() }}") {
    val sourceRoot = rootProject.file("companion/src/$sourceSet")
    val outputRoot = layout.buildDirectory.dir("generated/companion/$sourceSet")
    inputs.dir(sourceRoot)
    inputs.file(rootProject.file("stonecutter.gradle.kts"))
    inputs.property("minecraftVersion", mcversion)
    outputs.dir(outputRoot)
    doLast {
        val target = outputRoot.get().asFile.canonicalFile
        check(target.toPath().startsWith(layout.buildDirectory.get().asFile.canonicalFile.toPath()))
        // Only clear this task's generated files so removed sources cannot survive in a jar.
        delete(target)
        sourceRoot.walkTopDown().filter { it.isFile && it.extension in setOf("java", "kt") }.forEach { source ->
            val relative = source.relativeTo(sourceRoot).invariantSeparatorsPath
            stonecutter.process(source, "build/generated/companion/$sourceSet/$relative")
        }
    }
}
val companionMain = companionSources("main")
val companionTest = companionSources("test")
sourceSets {
    main {
        java.setSrcDirs(listOf(companionMain.map { layout.buildDirectory.dir("generated/companion/main/java").get() }))
        kotlin.setSrcDirs(emptyList<String>())
        resources.setSrcDirs(listOf(rootProject.file("companion/src/main/resources")))
    }
    test {
        java.setSrcDirs(emptyList<String>())
        kotlin.setSrcDirs(listOf(companionTest.map { layout.buildDirectory.dir("generated/companion/test/kotlin").get() }))
        resources.setSrcDirs(listOf(rootProject.file("companion/src/test/resources")))
    }
}
tasks.named("compileJava") { dependsOn(companionMain) }
tasks.named("compileTestKotlin") { dependsOn(companionTest) }
tasks.matching { it.name == "sourcesJar" }.configureEach { dependsOn(companionMain) }

repositories {
    mavenCentral()
    google()
    maven("https://repo.polyfrost.org/releases")
    maven("https://repo.polyfrost.org/snapshots")
    maven("https://central.sonatype.com/repository/maven-snapshots") {
        content { includeGroup("net.kyori") }
    }
    maven("https://maven.cloverclient.com/releases")
    maven("https://maven.deftu.dev/releases")
    maven("https://maven.fabricmc.net/")
    maven("https://maven.terraformersmc.com/")
    maven("https://api.modrinth.com/maven") {
        content { includeGroup("maven.modrinth") }
    }
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    minecraft("com.mojang:minecraft:$mcversion")
    if (isOrnithe) {
        mappings(ploceus!!.layeredMappings {
            mappings("net.ornithemc:feather-gen2:$mcversion+build.${sc.properties["feather_build"] as String}:v2") {
                containsUnpick()
            }
            mappings(rootProject.file("mappings/feather-overrides.tiny"))
        })
        testCompileOnly("net.ornithemc.osl-gen2:entrypoints:${sc.properties["deps.osl_entrypoints"] as String}")
    } else {
        loomx.applyMojangMappings()
    }

    modImplementation("net.fabricmc:fabric-loader:$loaderversion")
    // Use the published original jar in development and tests; do not bundle it.
    modImplementation("maven.modrinth:61g97TVF:${sc.properties["deps.overflowparticles_release"] as String}")
    modImplementation("org.polyfrost.oneconfig:$mcversion-$loader:$oneconfigversion")
    for (module in arrayOf("config", "config-impl", "events", "internal", "ui", "utils")) {
        implementation("org.polyfrost.oneconfig:$module:$oneconfigversion")
    }
    sc.properties.getOrNull<String>("deps.fabric_api")?.let {
        modImplementation("net.fabricmc.fabric-api:fabric-api:$it")
    }
    testImplementation("org.junit.jupiter:junit-jupiter:${sc.properties.get<String>("deps.junit")}")
    testImplementation("net.fabricmc:fabric-loader-junit:$loaderversion")
}

loom {
    fabricModJsonPath = rootProject.file("companion/src/main/resources/fabric.mod.json")
    runConfigs.all {
        preferGradleTask = true
        generateRunConfig = true
        runDirectory = rootProject.file("run")
        jvmArguments.add("-Dmixin.debug.export=true")
    }
    runConfigs.remove(runConfigs["server"])
}

java {
    withSourcesJar()
    toolchain {
        vendor = JvmVendorSpec.ADOPTIUM
        languageVersion = JavaLanguageVersion.of(requiredJava)
    }
}
tasks.withType<JavaCompile>().configureEach { options.release = requiredJava }
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.jvmTarget = JvmTarget.fromTarget(requiredJava.toString())
}

tasks {
    jar { from(rootProject.file("LICENSE")) }
    test {
        useJUnitPlatform()
        testLogging {
            showStackTraces = true
            exceptionFormat = TestExceptionFormat.FULL
        }
    }
    if (!isOrnithe) processTestResources { exclude("log4j2.xml") }
    processResources {
        val props = mapOf(
            "mod_version" to modversion,
            "mc_version" to sc.properties.get<String>("mod.mc_compat"),
            "loader_version" to loaderversion,
            "upstream_version" to upstreamversion,
            "java_version" to "JAVA_$requiredJava"
        )
        inputs.properties(props)
        filesMatching(listOf("fabric.mod.json", "mixins.$modid.json")) { expand(props) }
    }
    register<Copy>("buildAndCollect") {
        group = "build"
        from(loomx.modJar.flatMap { it.archiveFile }, loomx.modSourcesJar.flatMap { it.archiveFile })
        into(rootProject.layout.buildDirectory.dir("libs/$modversion"))
    }
}
