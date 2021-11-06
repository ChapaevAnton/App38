package com.w4eret1ckrtb1tch.app38

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.w4eret1ckrtb1tch.app38.databinding.ActivityDatabaseBinding
import com.w4eret1ckrtb1tch.app38.db.room.Address
import com.w4eret1ckrtb1tch.app38.db.room.BedEntity
import com.w4eret1ckrtb1tch.app38.db.room.CatDataBase
import com.w4eret1ckrtb1tch.app38.db.room.CatEntity
import java.util.*

class RoomActivity : AppCompatActivity() {

    private var _binding: ActivityDatabaseBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: CatDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDatabaseBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val address = Address(city = "Кемерово", street = "пр.Ленина", houseNumber = "777")
        val cat = CatEntity(
            name = "Василий",
            color = Color.RED,
            age = 1,
            address = address,
            birthDay = Date()
        )
        val bed = BedEntity(model = "MK1", idCat = 1)
        val cats =
            listOf(
                cat
            )

        val hashMd = HashMd()
        Log.d("TAG", "onCreate: ${hashMd.getHashMd5v1("1","1")}")
        Log.d("TAG", "onCreate: ${hashMd.getHashMd5v2("1","1")}")

        database = CatDataBase.getCatDataBase(this)
        database.catDao().insert(cat, bed)

        binding.add.setOnClickListener {
            val cat = CatEntity(name = binding.editData.text.toString(), address = address)
            database.catDao().insertCat(cat)
        }
        binding.update.setOnClickListener {
            runOnUiThread {
                val value = database.catDao().selectCat(1)
                binding.info.text = value.toString()
            }
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