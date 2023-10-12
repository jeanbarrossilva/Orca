import com.android.build.gradle.LibraryExtension
import com.jeanbarrossilva.orca.chrynan
import com.jeanbarrossilva.orca.loadable
import com.jeanbarrossilva.orca.namespace
import com.jeanbarrossilva.orca.parentNamespaceCandidate
import com.ncorti.ktfmt.gradle.KtfmtExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.maps.secrets) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.symbolProcessor) apply false
    alias(libs.plugins.ktfmtGradle) apply false
    alias(libs.plugins.moduleDependencyGraph)

    id("build-src")
}

subprojects subproject@{
    tasks.withType<KotlinCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.get()))
            freeCompilerArgs.addAll("-Xcontext-receivers", "-Xstring-concat=inline")
        }
    }

    repositories {
        chrynan()
        google()
        gradlePluginPortal()
        loadable(project)
        mavenCentral()
    }

    afterEvaluate {
        apply(plugin = libs.plugins.ktfmtGradle.get().pluginId)

        with(JavaVersion.toVersion(libs.versions.java.get())) java@{
            extensions.findByType<JavaPluginExtension>()?.apply {
                sourceCompatibility = this@java
                targetCompatibility = this@java
            }

            extensions.findByType<LibraryExtension>()?.apply {
                compileSdk = libs.versions.android.sdk.target.get().toInt()
                defaultConfig.minSdk = libs.versions.android.sdk.min.get().toInt()
                setDefaultNamespaceIfUnsetAndEligible(this@subproject)

                buildTypes {
                    release {
                        isMinifyEnabled = true
                    }
                }

                compileOptions {
                    sourceCompatibility = this@java
                    targetCompatibility = this@java
                }
            }
        }

        extensions.findByType<KtfmtExtension>()?.googleStyle()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

/**
 * Sets a default [namespace] to the [project] if it doesn't have one and the [project]'s
 * coordinate candidates are eligible to be part of the one to be set.
 *
 * @param project [Project] whose [namespace] will be set if unset.
 * @see isEligibleForNamespace
 **/
fun LibraryExtension.setDefaultNamespaceIfUnsetAndEligible(project: Project) {
    if (
        namespace == null &&
        isEligibleForNamespace(project.name) &&
        isEligibleForNamespace(project.parentNamespaceCandidate)
    ) {
        namespace = createDefaultNamespace(project)
    }
}

/**
 * Whether the [coordinates] are valid namespace coordinates.
 *
 * @param coordinates Dot-separated coordinates whose eligibility will be checked.
 **/
fun isEligibleForNamespace(coordinates: String): Boolean {
    return coordinates.all {
        it.isLetterOrDigit() || it == '.'
    }
}

/**
 * Creates a default [namespace] for the [project].
 *
 * @param project [Project] for which the default [namespace] will be created.
 * @return Created [namespace].
 **/
fun createDefaultNamespace(project: Project): String {
    return "${rootProject.namespace}." + project.parentNamespaceCandidate + ".${project.name}"
}
