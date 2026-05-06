plugins {
    id(ThunderbirdPlugins.Library.kmp)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "net.thunderbird.feature.navigation.changelog.api"
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            implementation(projects.core.ui.navigation)
        }
    }
}

codeCoverage {
    branchCoverage = 0
    lineCoverage = 0
}
