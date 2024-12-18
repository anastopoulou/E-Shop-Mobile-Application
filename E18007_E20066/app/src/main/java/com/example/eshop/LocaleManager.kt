package com.example.eshop

import android.content.Context
import android.content.SharedPreferences
import java.util.Locale

class LocaleManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
    }

    var language: String?
        get() = prefs.getString(SELECTED_LANGUAGE, Locale.getDefault().language)
        set(language) {
            prefs.edit().putString(SELECTED_LANGUAGE, language).apply()
        }

    fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}
