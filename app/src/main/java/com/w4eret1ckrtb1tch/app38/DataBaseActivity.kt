package com.w4eret1ckrtb1tch.app38

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.w4eret1ckrtb1tch.app38.databinding.ActivityDatabaseBinding
import com.w4eret1ckrtb1tch.app38.db.DataBase
import com.w4eret1ckrtb1tch.app38.db.Item

class DataBaseActivity : AppCompatActivity() {

    private var _binding: ActivityDatabaseBinding? = null
    private val binding get() = _binding!!
    private var dataBase: DataBase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDatabaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataBase = DataBase.getDataBase(context = this)
        dataBase?.addItem(Item("Молоко", 20.50))

    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}