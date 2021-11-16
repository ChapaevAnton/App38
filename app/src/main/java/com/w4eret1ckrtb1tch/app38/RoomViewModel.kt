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
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.coroutines.*

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


    private val scope1 = CoroutineScope(context = Dispatchers.Main)
    private val scope3 =
        CoroutineScope(Job() + Executors.newSingleThreadExecutor().asCoroutineDispatcher())
    private val scope4 = CoroutineScope(context = EmptyCoroutineContext)
    private val scope5 = CoroutineScope(context = Job() + CustomContext("1", "2"))

    private val job =
        MainScope().launch(start = CoroutineStart.LAZY, context = viewModelScope.coroutineContext) {
            delay(TimeUnit.SECONDS.toMillis(3L))
            _selectAllCat.value = selectAll()
            ensureActive()
            Log.d("TAG", "selectAllCat: ok active:${isActive}")
        }
    private val scope2 = CoroutineScope(job)

    fun selectAllCat() {
        Log.d("TAG", "selectAllCat scope1: ${contextToString(scope1.coroutineContext)}")
        Log.d("TAG", "selectAllCat scope3: ${contextToString(scope3.coroutineContext)}")
        Log.d("TAG", "selectAllCat scope4: ${contextToString(scope4.coroutineContext)}")
        Log.d("TAG", "selectAllCat scope5: ${contextToString(scope5.coroutineContext)}")
        scope1.launch {
            val deferred = async {
                return@async selectAll()
            }
            val result = deferred.await()
            _selectAllCat.value = result
            repeat(7) {
                scope3.launch {
                    Log.d("TAG", "!!!#$it started in ${Thread.currentThread().name}")
                    Thread.sleep(100)
                }
            }
            scope5.launch {
                val custom = coroutineContext[CustomContext]?.copy(str1 = "100", str2 = "200")
                Log.d("TAG", "selectAllCat: custom $custom")
                launch(start = CoroutineStart.LAZY) {
                    Log.d(
                        "TAG",
                        "selectAllCat: custom ${contextToString(coroutineContext[CustomContext]!!)}"
                    )
                }
            }
        }
    }

    fun cancelSelectCat() {
        //job.cancel()
        //scope2.cancel()
        Log.d("TAG", "cancelSelectCat: ${viewModelScope.isActive}")
    }

    private fun contextToString(context: CoroutineContext): String =
        "!!!Job: ${context[Job]}, Dispatcher: ${context[ContinuationInterceptor]}, Custom: ${context[CustomContext]}"

    private suspend fun insert(name: String) {
        return suspendCoroutine { continuation ->
            database.catDao().insertCat(CatEntity(name = name))
            continuation.resume(Unit)
        }
    }

    private suspend fun selectAll(): List<CatEntity> {
        return suspendCoroutine { continuation ->
            thread {
                Log.d("TAG", "selectAll: ok")
                val cats = database.catDao().selectAll()
                continuation.resume(cats)
            }
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

data class CustomContext(
    val str1: String,
    val str2: String
) : AbstractCoroutineContextElement(CustomContext) {
    companion object Key : CoroutineContext.Key<CustomContext>
}