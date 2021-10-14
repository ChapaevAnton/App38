package com.w4eret1ckrtb1tch.app38

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.content.getSystemService
import com.w4eret1ckrtb1tch.app38.databinding.ActivityMainBinding
import java.io.File
import java.util.concurrent.Executors

const val SETTINGS = "settings"

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    // TODO: https://developer.android.com/guide/topics/data
    //  https://developer.android.com/reference/android/os/Environment
    //  https://developer.android.com/reference/android/content/ContextWrapper
    //  https://developer.android.com/reference/android/os/storage/StorageManager

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

        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            Executors.newSingleThreadExecutor().execute {
                val numBytesNeeded = 1024 * 1024 * 10L
                val storageManager = applicationContext.getSystemService<StorageManager>()!!
                val specificInternalDirUuid =
                    storageManager.getUuidForPath(getExternalFilesDir(null)!!)
                val availableBytes: Long =
                    storageManager.getAllocatableBytes(specificInternalDirUuid)

                if (availableBytes >= numBytesNeeded) {
                    storageManager.allocateBytes(specificInternalDirUuid, numBytesNeeded)
                    // TODO: write data
                    Log.d("TAG", "availableBytes external: ${availableBytes / 1024 / 1024}mb")
                } else {
                    val storageIntent = Intent().apply {
                        action = StorageManager.ACTION_MANAGE_STORAGE
                    }
                    startActivity(storageIntent)
                }
            }
        }

        // TODO: 06.10.2021 38.5. Permissions
        binding.permission.setOnClickListener {
            getPermission()
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        clearCache(cacheDir)
        clearCache(externalCacheDir)

        sharedPreference?.unregisterOnSharedPreferenceChangeListener(sharedListener)
        sharedPreference = null
        _binding = null
    }

    // TODO: Deprecate
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_WRITE_EXTERNAL -> {
                grantResults.forEach {
                    if (grantResults.isNotEmpty() && it == PackageManager.PERMISSION_GRANTED) {
                        getPermission()
                    }
                }
            }
        }

    }

    private fun clearCache(files: File?) {
        val listFiles = files?.listFiles()
        listFiles?.forEach { file ->
            Log.d("TAG", "cash_delete: ${file.name} ")
            file.delete()
        }
    }

    val REQUEST_CODE_WRITE_EXTERNAL = 1

    private fun getPermission() {
        if (checkPermission()) {
            // TODO: make work
            Toast.makeText(this, "check", Toast.LENGTH_SHORT).show()
            // TODO: 14.10.2021 38.6 MediaStore
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.cat)
            loadBitmap(bitmap)
        } else {
            requestPermission()
        }
    }

    // TODO: 14.10.2021 38.6 MediaStore
    private fun loadBitmap(bitmap: Bitmap?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "Кот")
                put(MediaStore.Images.Media.DESCRIPTION, "Черный котенок")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp")
            }
            val contentResolver = contentResolver
            val uri =
                contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )!!
            contentResolver.openOutputStream(uri).use {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    bitmap?.compress(Bitmap.CompressFormat.WEBP_LOSSLESS, 100, it)
                } else {
                    bitmap?.compress(Bitmap.CompressFormat.WEBP, 100, it)
                }
            }
        } else {
            MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Кот", "Черный кот")
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_CODE_WRITE_EXTERNAL
        )
    }
}