package app.k9mail.feature.funding.api

import app.k9mail.core.ui.compose.navigation.Navigation
import app.k9mail.core.ui.compose.navigation.Route
import kotlinx.serialization.Serializable

const val FUNDING_BASE_DEEP_LINK = "app://feature/funding"

sealed interface FundingRoute : Route {
    @Serializable
    data object Contribution : FundingRoute {
        override val basePath: String = BASE_PATH

        override fun route(): String = basePath

        const val BASE_PATH = "$FUNDING_BASE_DEEP_LINK/contribution"
    }
}

interface FundingNavigation : Navigation<FundingRoute>
