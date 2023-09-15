package com.example.shared_preferences_simple_example

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shared_preferences_simple_example.databinding.ActivityMainBinding

internal class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupListeners()
    }

    private fun setupListeners() {
        val sharedPreference = getSharedPreferences(Keys.sharedPrefName, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()

        binding.saveButton.setOnClickListener { saveConfigs(editor) }
        binding.loadButton.setOnClickListener { getSavedConfigs(sharedPreference) }
    }

    private fun saveConfigs(editor: SharedPreferences.Editor?) {
        val name = binding.inputName.text.toString()
        val age = binding.inputAge.text.toString().toInt()
        val isAdult = binding.checkBox.isChecked

        editor?.apply {
            putString(Keys.name, name)
            putInt(Keys.age, age)
            putBoolean(Keys.isAdult, isAdult)
            apply()
        }
    }

    private fun getSavedConfigs(sharedPreference: SharedPreferences) {
        val name = sharedPreference.getString(Keys.name, null)
        val age = sharedPreference.getInt(Keys.age, 0)
        val isAdult = sharedPreference.getBoolean(Keys.isAdult, false)

        binding.inputName.setText(name)
        binding.inputAge.setText(age.toString())
        binding.checkBox.isChecked = isAdult
    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private object Keys {
        const val sharedPrefName = "mySharedPref"
        const val name = "name"
        const val age = "age"
        const val isAdult = "isAdult"
    }
}