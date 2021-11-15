package com.w4eret1ckrtb1tch.app38

import android.app.Application
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.*
import com.w4eret1ckrtb1tch.app38.db.room.Address
import com.w4eret1ckrtb1tch.app38.db.room.BedEntity
import com.w4eret1ckrtb1tch.app38.db.room.CatDataBase
import com.w4eret1ckrtb1tch.app38.db.room.CatEntity
import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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
        val insertJob = viewModelScope.launch {
            insert(name)
        }
    }

    private val scope1 = CoroutineScope(context = MainScope().coroutineContext)

    private val job =
        MainScope().launch(start = CoroutineStart.LAZY, context = viewModelScope.coroutineContext) {
            delay(TimeUnit.SECONDS.toMillis(3L))
            _selectAllCat.value = selectAll()
            ensureActive()
            Log.d("TAG", "selectAllCat: ok active:${isActive}")
        }
    private val scope2 = CoroutineScope(job)

    fun selectAllCat() {
        scope1.launch {
            val deferred = async {
                return@async selectAll()
            }
            val result = deferred.await()
            _selectAllCat.value = result
        }

    }

    fun cancelSelectCat() {
        //job.cancel()
        //scope2.cancel()
        Log.d("TAG", "cancelSelectCat: ${viewModelScope.isActive}")
    }

    private suspend fun insert(name: String) {
        return suspendCoroutine { continuation ->
            database.catDao().insertCat(CatEntity(name = name))
            continuation.resume(Unit)
        }
    }

    private suspend fun selectAll(): List<CatEntity> {
        return suspendCoroutine { continuation ->
            val cats = database.catDao().selectAll()
            continuation.resume(cats)
        }
    }

    private val _selectLastCat: LiveData<CatEntity> = MutableLiveData()
    val selectLastCat: LiveData<String> =
        Transformations.map(_selectLastCat) { cat -> "${cat.name} ${cat.id}" }

    private val _selectAllCat: MutableLiveData<List<CatEntity>> = MutableLiveData()
    val selectAllCat: LiveData<List<CatEntity>> = _selectAllCat


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