package app.k9mail.feature.navigation.drawer.domain

import app.k9mail.feature.navigation.drawer.NavigationDrawerExternalContract.DrawerConfig
import app.k9mail.feature.navigation.drawer.domain.entity.DisplayAccount
import app.k9mail.feature.navigation.drawer.domain.entity.DisplayFolder
import app.k9mail.legacy.account.Account
import kotlinx.coroutines.flow.Flow

internal interface DomainContract {

    interface UseCase {
        fun interface GetDrawerConfig {
            operator fun invoke(): Flow<DrawerConfig>
        }

        fun interface GetDisplayAccounts {
            operator fun invoke(): Flow<List<DisplayAccount>>
        }

        fun interface GetDisplayFoldersForAccount {
            operator fun invoke(accountUuid: String, includeUnifiedFolders: Boolean): Flow<List<DisplayFolder>>
        }

        /**
         * Synchronize the given account.
         */
        fun interface SyncAccount {
            operator fun invoke(account: Account): Flow<Result<Unit>>
        }

        /**
         * Synchronize all accounts.
         */
        fun interface SyncAllAccounts {
            operator fun invoke(): Flow<Result<Unit>>
        }
    }
}