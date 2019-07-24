package org.mjstudio.ggonggang.util

import android.app.Application
import android.content.Context

class SPService(private val context: Application) {

    private val packageName: String = context.packageName

    fun saveString(key: String, value: String) {
        val preferences = context.getSharedPreferences(packageName, Context.MODE_PRIVATE)
        preferences.edit().putString(key, value).apply()
    }

    fun saveBoolean(key: String, value: Boolean) {
        val preferences = context.getSharedPreferences(packageName, Context.MODE_PRIVATE)
        preferences.edit().putBoolean(key, value).apply()
    }

    fun saveInt(key: String, value: Int) {
        val preferences = context.getSharedPreferences(packageName, Context.MODE_PRIVATE)
        preferences.edit().putInt(key, value).apply()
    }

    fun saveDouble(key: String, value: Float) {
        val preferences = context.getSharedPreferences(packageName, Context.MODE_PRIVATE)
        preferences.edit().putFloat(key, value).apply()
    }

    fun loadString(key: String): String? {
        val preferences = context.getSharedPreferences(packageName, Context.MODE_PRIVATE)

        return preferences.getString(key, null)
    }

    fun loadBoolean(key: String): Boolean {
        val preferences = context.getSharedPreferences(packageName, Context.MODE_PRIVATE)

        return preferences.getBoolean(key, false)
    }

    fun loadInt(key: String): Int {
        val preferences = context.getSharedPreferences(packageName, Context.MODE_PRIVATE)

        return preferences.getInt(key, 0)
    }

    fun loadFloat(key: String): Float {
        val preferences = context.getSharedPreferences(packageName, Context.MODE_PRIVATE)

        return preferences.getFloat(key, 0f)
    }
}