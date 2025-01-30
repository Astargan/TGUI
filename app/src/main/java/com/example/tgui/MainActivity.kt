package com.example.tgui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.tgui.Transliterator
import androidx.core.view.isGone
import androidx.core.view.isVisible

class MainActivity : AppCompatActivity() {

    private val inputText: EditText by lazy { findViewById(R.id.editText) }
    private val spinner: Spinner by lazy { findViewById(R.id.spinner) }
    private val translateButton: Button by lazy { findViewById(R.id.button) }
    private val outputText: TextView by lazy { findViewById(R.id.resultTextView) }
    private val splashScreen: LinearLayout by lazy { findViewById(R.id.splashScreen) }
    private val mainContent: LinearLayout by lazy { findViewById(R.id.mainContent) }
    private val clearButton: ImageButton by lazy { findViewById(R.id.clear_text) }
    private val buttonCopy: ImageButton by lazy { findViewById(R.id.copy_result) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        hideSplashScreen()
        setupListeners()
        setupSpinner()
    }

    private fun hideSplashScreen() {
        splashScreen.animate()
            .alpha(0f)
            .setDuration(3000)
            .withEndAction {
                splashScreen.isGone = true
                mainContent.isVisible = true
            }
    }

    private fun setupListeners() {
        clearButton.setOnClickListener {
            inputText.text.clear()
            outputText.text = ""
        }

        buttonCopy.setOnClickListener {
            copyToClipboard(outputText.text.toString())
        }

        translateButton.setOnClickListener {
            if (inputText.text.isEmpty()) {
                showToast(getString(R.string.hint_text))
            } else {
                performTranslation(inputText.text.toString())
            }
        }

        inputText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                performTranslation(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupSpinner() {
        val translationOptions = resources.getStringArray(R.array.traslate_spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, translationOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun performTranslation(text: String) {
        val selectedOption = spinner.selectedItem.toString()
        val result = when (selectedOption) {
            getString(R.string.trans_cyr_to_ar) -> Transliterator.transliterateToArabic(text)
            getString(R.string.trans_cyr_to_lat) -> Transliterator.transliterateToLatin(text)
            else -> getString(R.string.trans_mode_error)
        }
        outputText.text = result.toString()
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("label", text))
        showToast(getString(R.string.text_copied))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }

            R.id.action_exit -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LanguageManager.attachBaseContext(newBase))
    }
}