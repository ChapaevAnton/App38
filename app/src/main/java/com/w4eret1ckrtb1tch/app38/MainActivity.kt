package com.w4eret1ckrtb1tch.app38

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.w4eret1ckrtb1tch.app38.databinding.ActivityMainBinding

const val SETTINGS = "settings"

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var sharedPreference: SharedPreferences? = null
    private var sharedListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreference = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
        sharedPreference?.edit {
            putString("str_key", "string")
            putInt("int_key", 1)
            putBoolean("boolean_key", true)
        }
        try {
            val int: Int = sharedPreference?.getInt("int_key", 0)!!
            Log.d("TAG", "sharedPreference: $int")
        } catch (error: ClassCastException) {
            error.printStackTrace()
        }
        binding.button.setOnClickListener {
            sharedPreference?.edit {
                putString("str_key", binding.editText.text.toString())
            }
            Log.d("TAG", "sharedPreference: ${sharedPreference?.all}")
        }
        sharedListener =
            SharedPreferences.OnSharedPreferenceChangeListener { shared, key ->
                when (key) {
                    "str_key" -> binding.textView.text = shared.getString(key, "default")
                }
            }
        sharedPreference?.registerOnSharedPreferenceChangeListener(sharedListener)

    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPreference?.unregisterOnSharedPreferenceChangeListener(sharedListener)
        sharedPreference = null
        _binding = null
    }
}