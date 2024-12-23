package com.example.tgui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import android.widget.ImageButton
import android.widget.EditText
import android.widget.Button
import android.text.Editable
import android.text.TextWatcher



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Элементы интерфейса
        val inputText: EditText = findViewById(R.id.editText)

        val spinner: Spinner = findViewById(R.id.spinner)
        val translateButton: Button = findViewById(R.id.button)
        val outputText: TextView = findViewById(R.id.resultTextView)
        val splashScreen: LinearLayout = findViewById(R.id.splashScreen)
        val mainContent: LinearLayout = findViewById(R.id.mainContent)
//кнопка очистки текста в обоих полях
        val editText: EditText = findViewById(R.id.editText)
        val textView: TextView = findViewById(R.id.resultTextView)
        val clearButton: ImageButton = findViewById(R.id.clear_text)
        clearButton.setOnClickListener {
            editText.text.clear()
            textView.text = ""
        }

        val resultTextView: TextView = findViewById(R.id.resultTextView)
        val buttonCopy: ImageButton = findViewById(R.id.copy_result)
            buttonCopy.setOnClickListener {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label", textView.text)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this, getString(R.string.text_copied), Toast.LENGTH_SHORT).show()

        }

        // Приветственный экран
        splashScreen.animate().alpha(0f).setDuration(3000).withEndAction {
            splashScreen.visibility = LinearLayout.GONE // Скрыть приветственный экран
            mainContent.visibility = LinearLayout.VISIBLE // Показать основной экран

        }

        // Спинер транслита
        val translationOptions = resources.getStringArray(R.array.traslate_spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, translationOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        translateButton.setOnClickListener {
            val text = inputText.text.toString()
            val selectedOption = spinner.selectedItem.toString()

            if (text.isEmpty()) {
                Toast.makeText(this, getString(R.string.hint_text), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Выбор метода транслитерации
            val result = when (selectedOption) {
                getString(R.string.trans_cyr_to_ar) -> transliterateToArabic(text)
                getString(R.string.trans_cyr_to_lat) -> transliterateToLatin(text)
                else -> getString(R.string.trans_mode_error)
            }
            outputText.text = result
        }

        // Перевод в реальном времени
        inputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()
                if (text.isNotEmpty()) {
                    val selectedOption = spinner.selectedItem.toString()
                    val result = when (selectedOption) {
                        getString(R.string.trans_cyr_to_ar) -> transliterateToArabic(text)
                        getString(R.string.trans_cyr_to_lat) -> transliterateToLatin(text)
                        else -> getString(R.string.trans_mode_error)
                    }
                    outputText.text = result
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        //Тулбар
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu) // Подключаем XML меню
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }

            R.id.action_exit -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }
}

    // Арабица
    private fun transliterateToArabic(input: String): String {
        val map = mapOf(
            ';' to '؛', ',' to '،', 'а' to 'ا', 'б' to 'پ', 'в' to 'ۋ', 'ӑ' to 'أ', 'ă' to 'أ',
            'е' to 'ە', 'з' to 'س', 'ĕ' to 'ۀ', 'ӗ' to 'ۀ', 'и' to 'ې', 'й' to 'ي',
            'к' to 'ك', 'л' to 'ل', 'м' to 'م', 'н' to 'ن', 'о' to 'و', 'п' to 'پ',
            'р' to 'ر', 'с' to 'س', 'ç' to 'ج', 'ҫ' to 'ج', 'т' to 'ت', 'у' to 'و',
            'ӳ' to 'ۆ', 'ф' to 'ف', 'х' to 'خ', 'ц' to 'ت', 'ч' to 'چ', 'ш' to 'ش',
            'ы' to 'ى', 'э' to 'ە', 'ю' to 'ي', 'я' to 'ي', 'ь' to 'ٰ', 'ъ' to ' ',
            '?' to '؟', 'А' to 'ا', 'Б' to 'پ', 'В' to 'ۋ', 'Ă' to 'أ', 'Ӑ' to 'أ',
            'Е' to 'ە', 'Ĕ' to 'ۀ', 'Ӗ' to 'ۀ', 'З' to 'س', 'И' to 'ې', 'Й' to 'ي',
            'К' to 'ك', 'Л' to 'ل', 'М' to 'م', 'Н' to 'ن', 'О' to 'و', 'П' to 'پ',
            'Р' to 'ر', 'С' to 'س', 'Ç' to 'ج', 'Ҫ' to 'ج', 'Т' to 'ت', 'У' to 'و',
            'Ӳ' to 'ۆ', 'Ф' to 'ف', 'Х' to 'خ', 'Ц' to 'ت', 'Ч' to 'چ', 'Ш' to 'ش',
            'Ы' to 'ى', 'Э' to 'ە', 'Ю' to 'ي', 'Я' to 'ي', 'Ь' to 'ٰ', 'Ъ' to ' ',
            '?' to '؟', 'Г' to 'ك', 'Д' to 'ت', 'г' to 'ك', 'д' to 'ت'
        )
        return input.map { map[it] ?: it }.joinToString("")
    }

    // Латиница Аргаду
    private fun transliterateToLatin(input: String): String {
        val map = mapOf(
            ',' to ",", 'а' to "a", 'б' to "p", 'в' to "v", 'г' to "k", 'д' to "t",
            'ӑ' to "o", 'ă' to "o", 'е' to "e", 'ж' to "ş", 'з' to "s",
            'ĕ' to "ö", 'ӗ' to "ö", 'и' to "i", 'й' to "y", 'к' to "k", 'л' to "l",
            'м' to "m", 'н' to "n", 'о' to "o", 'п' to "p", 'р' to "r", 'с' to "s",
            'ç' to "c", 'ҫ' to "c", 'т' to "t", 'у' to "u", 'ӳ' to "ü", 'ÿ' to "ü",
            'ф' to "f", 'х' to "x", 'ц' to "ts", 'ч' to "ç", 'щ' to "şç", 'ш' to "ş", 'ы' to "ı",
            'э' to "e", 'ю' to "yu", 'я' to "ya", 'ь' to "′", 'ъ' to "", '?' to "?",
            'А' to "A", 'Б' to "P", 'В' to "V", 'Г' to "K", 'Д' to "T", 'Ă' to "O",
            'Ӑ' to "O", 'Е' to "E", 'Ĕ' to "Ö", 'Ӗ' to "Ö", 'Ж' to "Ş", 'З' to "S",
            'И' to "İ", 'Й' to "Y", 'К' to "K", 'Л' to "L", 'М' to "M", 'Н' to "N",
            'О' to "O", 'П' to "P", 'Р' to "R", 'С' to "S", 'Ç' to "C", 'Ҫ' to "C",
            'Т' to "T", 'У' to "U", 'Ӳ' to "Ü", 'Ÿ' to "Ü", 'Ф' to "F", 'Х' to "X",
            'Ц' to "Ts", 'Ч' to "Ç", 'Щ' to "Şç", 'Ш' to "Ş", 'Ы' to "I", 'Э' to "E", 'Ю' to "Yu",
            'Я' to "Ya", 'Ь' to "′", 'Ъ' to "", '?' to "?"
        )
        return input.map { map[it] ?: it }.joinToString("")
    }
