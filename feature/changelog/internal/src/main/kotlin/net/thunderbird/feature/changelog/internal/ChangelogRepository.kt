package net.thunderbird.feature.changelog.internal

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import androidx.annotation.RawRes
import de.cketti.changelog.ChangeLog
import kotlinx.serialization.json.Json
import net.thunderbird.feature.changelog.internal.model.ChangelogIndex
import net.thunderbird.feature.changelog.internal.model.ChangelogRelease
import net.thunderbird.feature.navigation.changelog.api.ChangelogConfigProvider

class ChangelogRepository(
    private val context: Context,
    private val provider: ChangelogConfigProvider,
) {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    private  val LAST_VERSION_KEY: String = "ckChangeLog_last_version_code"
    private  val NO_VERSION: Int = -1

    val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun loadAllReleases(): List<ChangelogRelease> {
        val lastVersionCode = preferences.getInt(LAST_VERSION_KEY, NO_VERSION)
        println("EMON1234 lastVersionCode: $lastVersionCode")
        val index = readRawJson<ChangelogIndex>(provider.changelogIndexResId)

        return index.releases.mapNotNull { releaseEntry ->
            val resourceId = context.resources.getIdentifier(
                releaseEntry.resourceName,
                "raw",
                context.packageName,
            )

            if (resourceId == 0) {
                null
            } else {
                readRawJson<ChangelogRelease>(resourceId)
            }
        }
    }

    private inline fun <reified T> readRawJson(
        @RawRes resourceId: Int,
    ): T {
        val jsonString = context.resources.openRawResource(resourceId)
            .bufferedReader()
            .use { it.readText() }

        return json.decodeFromString(jsonString)
    }
}
