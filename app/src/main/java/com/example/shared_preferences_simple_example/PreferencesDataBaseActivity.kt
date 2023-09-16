package com.example.shared_preferences_simple_example

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.example.shared_preferences_simple_example.databinding.ActivityPreferencesDataBaseBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

internal class PreferencesDataBaseActivity : AppCompatActivity() {

    private var _binding: ActivityPreferencesDataBaseBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        dataStore = createDataStore(name = Data.dataStoreName)
        setupListeners()
    }

    private fun setupBinding() {
        _binding = ActivityPreferencesDataBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupListeners() {
        binding.goBack.setOnClickListener { finish() }
        binding.saveButton.setOnClickListener { saveConfigs() }
        binding.readResult.setOnClickListener { getSavedConfigs() }
    }

    private fun getSavedConfigs() {
        lifecycleScope.launch {
            val value = read(binding.keyInput.text.toString())
            binding.searchResultValue.text = value ?: Data.searchResultDefaultValue
        }
    }

    private fun saveConfigs() {
        lifecycleScope.launch {
            save(
                binding.keyInput.text.toString(),
                binding.valueInput.text.toString()
            )
        }
    }

    private suspend fun save(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String): String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private object Data {
        const val dataStoreName = "settings"
        const val searchResultDefaultValue = "No value found"
    }

    companion object {

        /**
         * Create a new intent for starting this activity.
         * @param context Requester context.
         */
        fun createIntent(context: Context): Intent =
            Intent(context, PreferencesDataBaseActivity::class.java)
    }
}