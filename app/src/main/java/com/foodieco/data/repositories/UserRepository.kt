package com.foodieco.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.foodieco.R
import com.foodieco.utils.toSha256
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val PROFILE_PIC_KEY = stringPreferencesKey("profile_pic")
        private val LOCATION_KEY = stringPreferencesKey("location")
    }

    val mapData: (Preferences.Key<String>, String) -> Flow<String> = { key, defaultValue ->
        dataStore.data.map { preferences ->
            preferences[key] ?: defaultValue
        }
    }

    val username = mapData(USERNAME_KEY, "")
    val password = mapData(PASSWORD_KEY, "")
    val profilePicture = mapData(PROFILE_PIC_KEY, "")
    val location = mapData(LOCATION_KEY, "")

    suspend fun setUsername(username: String) {
        dataStore.edit { preferences -> preferences[USERNAME_KEY] = username }
    }

    suspend fun setPassword(password: String) {
        dataStore.edit { preferences -> preferences[PASSWORD_KEY] = password.toSha256() }
    }

    suspend fun setProfilePicture(picture: R.drawable) {
        dataStore.edit { preferences -> preferences[PROFILE_PIC_KEY] = picture.toString() }
    }

    suspend fun setLocation(location: String) {
        dataStore.edit { preferences -> preferences[LOCATION_KEY] = location }
    }
}