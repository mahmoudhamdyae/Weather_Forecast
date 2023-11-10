package com.mahmoudhamdyae.weatherforecast.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStoreFile
import com.mahmoudhamdyae.weatherforecast.common.Constants
import com.mahmoudhamdyae.weatherforecast.domain.repository.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PreferencesRepositoryImpl (
    private val dataStore: DataStore<Preferences>
): PreferencesRepository {

    companion object {
        val IS_FIRST_TIME = booleanPreferencesKey("is_first_time")

        fun getPreferences(appContext: Context): PreferencesRepositoryImpl {
            val dataStore = PreferenceDataStoreFactory.create(
                corruptionHandler = ReplaceFileCorruptionHandler(
                    produceNewData = { emptyPreferences() }
                ),
                migrations = listOf(SharedPreferencesMigration(appContext, Constants.DATA_STORE_PREF_KEY)),
                scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
                produceFile = { appContext.preferencesDataStoreFile(Constants.DATA_STORE_PREF_KEY) }
            )
            return PreferencesRepositoryImpl(dataStore)
        }
    }

    override val isFirstTime: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            if (preferences[IS_FIRST_TIME] == null || preferences[IS_FIRST_TIME] == true) {
                setFirstTimePreference()
                true
            } else {
                false
            }
        }

    override suspend fun setFirstTimePreference() {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_TIME] = false
        }
    }
}