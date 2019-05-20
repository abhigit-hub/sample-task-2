package com.example.sampletask2.data.local.prefs

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(private val prefs: SharedPreferences) {

    companion object {
        const val KEY_IS_DATA_CONFIGURED = "KEY_IS_DATA_CONFIGURED"
        const val KEY_TAB_NAME = "KEY_TAB_NAME"
    }

    fun isDataConfigured(): Boolean = prefs.getBoolean(KEY_IS_DATA_CONFIGURED, false)

    fun setDataConfigured(status: Boolean) = prefs.edit().putBoolean(KEY_IS_DATA_CONFIGURED, status).apply()

    fun getTabName(): String? = prefs.getString(KEY_TAB_NAME, "")

    fun setTabName(name: String) = prefs.edit().putString(KEY_TAB_NAME, name).apply()
}