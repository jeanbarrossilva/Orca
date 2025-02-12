/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.symbolProcessor)
}

android {
  buildFeatures.compose = true
  lint.disable += "ProduceStateDoesNotAssignValue"
  testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
  api(project(":composite:composable"))

  implementation(project(":composite:timeline"))
  implementation(project(":platform:focus"))
  implementation(project(":platform:starter"))
  implementation(libs.android.compose.material.icons)
  implementation(libs.android.activity.ktx)

  ksp(project(":std:injector-processor"))

  testImplementation(project(":platform:autos-test"))
  testImplementation(project(":platform:navigation-test"))
  testImplementation(project(":platform:testing"))
  testImplementation(project(":std:injector-test"))
  testImplementation(libs.robolectric)
}
