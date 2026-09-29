package com.example.aarogya.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val AGE = intPreferencesKey("age")
        val GENDER = stringPreferencesKey("gender")
        val HEIGHT = intPreferencesKey("height")
        val WEIGHT = intPreferencesKey("weight")

        val NAME = stringPreferencesKey("NAME")
    }

    suspend fun saveUserData(
        age: Int,
        gender: String,
        height: Int,
        weight: Int,
        name: String,
    ) {
        context.dataStore.edit { prefs ->
            prefs[AGE] = age
            prefs[GENDER] = gender
            prefs[HEIGHT] = height
            prefs[WEIGHT] = weight
            prefs[NAME] = name
        }
    }

    val userData: Flow<Map<String, Any>> = context.dataStore.data.map { prefs ->
        mapOf(
            "age" to (prefs[AGE] ?: 0),
            "gender" to (prefs[GENDER] ?: ""),
            "height" to (prefs[HEIGHT] ?: 0),
            "weight" to (prefs[WEIGHT] ?: 0),
            "activity" to (prefs[NAME] ?: ""),
        )
    }
}
