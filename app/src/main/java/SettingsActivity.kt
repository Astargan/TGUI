package com.example.tgui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val spinnerLanguage = findViewById<Spinner>(R.id.spinner_language)
        val saveButton: Button = findViewById(R.id.btn_save_settings)

        // Настраиваем спиннер языков
        val languages =  resources.getStringArray (R.array.lang_spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLanguage.adapter = adapter

        // Загружаем сохранённый язык из SharedPreferences
        val sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("language", languages[0])
        spinnerLanguage.setSelection(languages.indexOf(savedLanguage))

        // Обработчик нажатия на кнопку "Сохранить"
        saveButton.setOnClickListener {
            val selectedLanguage = spinnerLanguage.selectedItem.toString()
            sharedPreferences.edit().putString("language", selectedLanguage).apply()

            // Устанавливаем и применяем новый язык
            applyLanguage(selectedLanguage)

            // Перезапускаем MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            // Показываем Toast сообщение
            Toast.makeText(this, getString(R.string.set_saved), Toast.LENGTH_SHORT).show()
        }
    }

    private fun applyLanguage(language: String) {
        val locale = when (language) {
            "Chuvash" -> Locale("cv")
            "English" -> Locale("en")
//            "Russian" -> Locale("ru", "RU")
            else -> Locale("en")
        }

        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
