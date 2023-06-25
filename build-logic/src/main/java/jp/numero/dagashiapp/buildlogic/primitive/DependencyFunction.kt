package jp.numero.dagashiapp.buildlogic.primitive

import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.getByType
import java.util.Optional
fun DependencyHandlerScope.implementation(
    artifact: Optional<Provider<MinimalExternalModuleDependency>>,
) = add("implementation", artifact.get())

fun DependencyHandlerScope.implementation(
    artifact: Provider<MinimalExternalModuleDependency>,
) = add("implementation", artifact)

internal fun DependencyHandlerScope.implementationBundles(
    dependency: Optional<Provider<ExternalModuleDependencyBundle>>,
) = add("implementation", dependency.get())

fun DependencyHandlerScope.debugImplementation(
    artifact: Optional<Provider<MinimalExternalModuleDependency>>,
) = add("debugImplementation", artifact.get())

fun DependencyHandlerScope.androidTestImplementation(
    artifact: Optional<Provider<MinimalExternalModuleDependency>>,
) = add("androidTestImplementation", artifact.get())

fun DependencyHandlerScope.testImplementation(
    artifact: Optional<Provider<MinimalExternalModuleDependency>>,
) = add("testImplementation", artifact.get())

fun DependencyHandlerScope.api(
    artifact: Optional<Provider<MinimalExternalModuleDependency>>,
) = add("api", artifact.get())

fun DependencyHandlerScope.kapt(
    dependency: Optional<Provider<MinimalExternalModuleDependency>>,
) = add("kapt", dependency.get())

fun DependencyHandlerScope.kaptTest(
    dependency: Optional<Provider<MinimalExternalModuleDependency>>,
) = add("kaptTest", dependency.get())

val Project.libs get() = extensions.getByType<VersionCatalogsExtension>().named("libs")