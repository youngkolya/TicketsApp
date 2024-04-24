@file:Suppress("DEPRECATION")

package com.example.tickets.functions

import android.content.Context
import android.preference.PreferenceManager

fun saveLastTown(context: Context, key: String, value: String) {
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    val editor = preferences.edit()
    editor.putString(key, value)
    editor.apply()
}

fun loadLastTown(context: Context, key: String):String?{
    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    return preferences.getString(key, null)
}