package com.sdk.preferencedatastore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.lifecycle.lifecycleScope
import com.sdk.preferencedatastore.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataStore = createDataStore("MyDataStore")

        binding.btnSave.setOnClickListener {
            val key = binding.etSaveKey.text.toString().trim()
            val value = binding.etSaveValue.text.toString().trim()
            lifecycleScope.launch {
                saveData(
                    key,
                    value
                )
            }
        }
        binding.btnRead.setOnClickListener {
            val key = binding.etReadkey.text.toString().trim()
            lifecycleScope.launch {
                binding.textView.text = readData(key) ?: "No Data"
            }
        }
    }

    private suspend fun saveData(key: String, value: String) {
        val dataStoreKey = preferencesKey<String>(key)
        dataStore.edit {
            it[dataStoreKey] = value
        }
    }

    private suspend fun readData(key: String): String? {
        val dataStoreKey = preferencesKey<String>(key)
        val preferenceKey = dataStore.data.first()
        return preferenceKey[dataStoreKey]
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}