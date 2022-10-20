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

    suspend fun saveToken(token: String) {
        storyDataStore.edit { preference ->
            preference[AUTHORIZATION_KEY] = token
        }
    }

    fun getToken(): Flow<String> {
        return storyDataStore.data.map { preference ->
            preference[AUTHORIZATION_KEY] ?: ""
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

    suspend fun deleteDataStore() {
        storyDataStore.edit { preference ->
            preference.clear()
        }
    }

    companion object {
        private val AUTHORIZATION_KEY = stringPreferencesKey("user_token")
        private val USER_SESSION = booleanPreferencesKey("user_session")
    }
}