package com.w4eret1ckrtb1tch.app38

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.w4eret1ckrtb1tch.app38.databinding.ActivityDatabaseBinding

class RoomActivity : AppCompatActivity() {

    private var _binding: ActivityDatabaseBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<RoomViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDatabaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.transformationsCount.observe(this) { count ->
            Log.d(
                "TAG",
                "transformationsCount: $count"
            )
        }
        viewModel.sourceMediator.observe(this) { count ->
            Log.d(
                "TAG",
                "sourceMediatorCount: $count"
            )
        }

        viewModel.selectLastCat.observe(this) { cat ->
            if (cat == null) return@observe
            binding.info.text = cat
        }

        viewModel.selectAllCat.observe(this) { cats ->
            if (cats == null) return@observe
            binding.info.text = cats.toString()
        }

        binding.add.setOnClickListener {
            val name = binding.editData.text.toString()
            viewModel.insertCat(name)
        }
        binding.update.setOnClickListener {
            viewModel.selectAllCat()
        }

        binding.delete.setOnClickListener {
            viewModel.cancelSelectCat()
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