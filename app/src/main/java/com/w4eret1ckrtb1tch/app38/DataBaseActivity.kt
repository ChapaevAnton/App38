package com.w4eret1ckrtb1tch.app38

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.w4eret1ckrtb1tch.app38.databinding.ActivityDatabaseBinding
import com.w4eret1ckrtb1tch.app38.db.DataBase
import com.w4eret1ckrtb1tch.app38.db.Item
import kotlin.random.Random

class DataBaseActivity : AppCompatActivity() {

    private var _binding: ActivityDatabaseBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataBase: DataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDatabaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataBase = DataBase.getDataBase(context = this)!!

        binding.add.setOnClickListener {
            val name = binding.editData.text.toString()
            dataBase.addItem(Item(name, Random.nextDouble()))
            getInfo()
        }
        binding.update.setOnClickListener {
            val name = binding.editData.text.toString()
            dataBase.updateItem(Item(name, Random.nextDouble()))
            getInfo()
        }

        binding.delete.setOnClickListener {
            val name = binding.editData.text.toString()
            dataBase.deleteItem(Item(name))
            getInfo()
        }
    }

    private fun getInfo() {
        val result = "database: ${dataBase.getItems()}"
        binding.info.text = result
        Log.d("TAG", "database: $result")
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}