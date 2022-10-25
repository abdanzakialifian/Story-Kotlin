package com.abdanzakialifian.storyapp.data.source.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.datastore by preferencesDataStore("story")

@Singleton
class StoryDataStore @Inject constructor(@ApplicationContext context: Context) {

    private val storyDataStore = context.datastore

    suspend fun saveUserData(token: String, name: String) {
        storyDataStore.edit { preference ->
            preference[AUTHORIZATION_KEY] = token
            preference[USER_NAME] = name
        }
    }

    fun getToken(): Flow<String> {
        return storyDataStore.data.map { preference ->
            preference[AUTHORIZATION_KEY] ?: ""
        }
    }

    fun getName(): Flow<String> {
        return storyDataStore.data.map { preference ->
            preference[USER_NAME] ?: ""
        }
    }

    suspend fun saveUserSession(isLogin: Boolean) {
        storyDataStore.edit { preference ->
            preference[USER_SESSION] = isLogin
        }
    }

    fun getUserSession(): Flow<Boolean> {
        return storyDataStore.data.map { preference ->
            preference[USER_SESSION] ?: false
        }
    }

    suspend fun saveLanguageCode(languageCode: String) {
        storyDataStore.edit { preference ->
            preference[LANGUAGE_CODE] = languageCode
        }
    }

    fun getLanguageCode(): Flow<String> {
        return storyDataStore.data.map { preference ->
            preference[LANGUAGE_CODE] ?: ""
        }
    }

    suspend fun deleteDataStore() {
        storyDataStore.edit { preference ->
            preference.clear()
        }
    }

    companion object {
        private val AUTHORIZATION_KEY = stringPreferencesKey("user_token")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_SESSION = booleanPreferencesKey("user_session")
        private val LANGUAGE_CODE = stringPreferencesKey("language_code")
    }
}