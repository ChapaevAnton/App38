package com.w4eret1ckrtb1tch.app38

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.w4eret1ckrtb1tch.app38.databinding.ActivityMainBinding
import java.io.File

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

        // TODO: 05.10.2021 SharedPreferences 
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

        // TODO: 05.10.2021 Internal Storage
        // TODO: 05.10.2021 context.deleteFile(cacheFileName) и
        //  context.openFileOutput(filename, Context.MODE_PRIVATE) для cache файлов не работает? почему?
        val fileInternal = File("temp_internal.txt")
        val content = "content_internal"
        openFileOutput(fileInternal.name, Context.MODE_PRIVATE).use { output ->
            output.write(content.toByteArray())
        }

        //v1
        openFileInput(fileInternal.name).bufferedReader().useLines { lines ->
            lines.forEach { Log.d("TAG", "useLines: $it ") }
        }
        //v2
        openFileInput(fileInternal.name).bufferedReader().forEachLine { string ->
            Log.d("TAG", "forEachLine: $string ")
        }

        File.createTempFile("cache_temp", null, cacheDir)
        val listFiles = cacheDir.listFiles()
        listFiles?.forEach { file ->
            Log.d("TAG", "cache_add: ${file.name} ")
            file.writeBytes(content.toByteArray())
        }

        // TODO: 06.10.2021 38.4. External Storage
        val content1 = "content_external"
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val fileExternal =
                File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "temp_external.txt")
            fileExternal.bufferedWriter().use { it.write(content1) }

            fileExternal.bufferedReader().forEachLine { string ->
                Log.d("TAG", "forEachLine: $string ")
            }

            val cashFile = File.createTempFile("cache_temp", null, externalCacheDir)
            cashFile.bufferedWriter().use { it.write(content1) }
            cashFile.bufferedReader().forEachLine { string ->
                Log.d("TAG", "forEachLine cache: $string ")
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        val listFiles = cacheDir.listFiles()
        listFiles?.forEach { file ->
            Log.d("TAG", "cash_delete: ${file.name} ")
            file.delete()
        }


        sharedPreference?.unregisterOnSharedPreferenceChangeListener(sharedListener)
        sharedPreference = null
        _binding = null
    }
}