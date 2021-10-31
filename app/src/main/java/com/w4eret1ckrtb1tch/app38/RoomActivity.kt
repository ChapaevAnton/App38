package com.w4eret1ckrtb1tch.app38

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.w4eret1ckrtb1tch.app38.databinding.ActivityDatabaseBinding
import com.w4eret1ckrtb1tch.app38.db.room.CatDataBase
import com.w4eret1ckrtb1tch.app38.db.room.CatEntity

class RoomActivity : AppCompatActivity() {

    private var _binding: ActivityDatabaseBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: CatDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDatabaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cats = listOf(CatEntity(name = "Василий", color = Color.RED, age = 1))

        database = CatDataBase.getCatDataBase(this)
        database.catDao().insertAll(cats)

        binding.add.setOnClickListener {
            val cat = CatEntity(name = binding.editData.text.toString())
            database.catDao().insert(cat)
        }
        binding.update.setOnClickListener {

        }

        binding.delete.setOnClickListener {

        }
    }

    private fun getInfo() {
        val result = ""
        binding.info.text = result
        Log.d("TAG", "database: $result")
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}