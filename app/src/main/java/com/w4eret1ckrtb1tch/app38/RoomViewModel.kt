package com.w4eret1ckrtb1tch.app38

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.*
import com.w4eret1ckrtb1tch.app38.db.room.Address
import com.w4eret1ckrtb1tch.app38.db.room.BedEntity
import com.w4eret1ckrtb1tch.app38.db.room.CatDataBase
import com.w4eret1ckrtb1tch.app38.db.room.CatEntity
import kotlinx.coroutines.launch
import java.util.*

class RoomViewModel(application: Application) : AndroidViewModel(application) {

    private val database: CatDataBase = CatDataBase.getCatDataBase(getApplication())

    private val _countOne = MutableLiveData<Int>()
    private val _countTwo = MutableLiveData<Int>()
    val countOne: LiveData<Int> get() = _countOne
    val countTwo: LiveData<Int> get() = _countTwo

    val transformationsCount: LiveData<String> =
        Transformations.map(_countOne) { count -> count.toString() }

    val sourceMediator = MediatorLiveData<String>()

    init {

        sourceMediator.addSource(_countOne) {
            sourceMediator.value = it.toString()
        }
        sourceMediator.addSource(_countTwo) {
            sourceMediator.value = it.toString()
        }

    }

    fun insertCat(name: String) {
        viewModelScope.launch {
            database.catDao().insertCat(CatEntity(name = name))
        }
    }

    private val _selectLastCat: LiveData<CatEntity> = database.catDao().selectCatLast()
    val selectLastCat: LiveData<String> =
        Transformations.map(_selectLastCat) { cat -> "${cat.name} ${cat.id}" }

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


}