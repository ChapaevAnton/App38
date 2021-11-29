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
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.coroutines.*
import kotlin.system.measureTimeMillis

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

    // TODO: 28.11.2021 41.1 Exceptions
    private val coroutineException = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d("TAG", "coroutineContext: ${coroutineContext.isActive}")
        Log.d("TAG", "ArithmeticException: ${throwable.printStackTrace()}")
    }
    private val scope6 = CoroutineScope(context = coroutineException)


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
        // TODO: 28.11.2021 41.1 Exceptions
        scope6.launch {

            launch(SupervisorJob(coroutineContext[Job]) + Dispatchers.Default) {
                val sum = 10 / 0
            }

            launch {
                repeat(5) {
                    delay(TimeUnit.SECONDS.toMillis(1L))
                    Log.d("TAG", "scope6: $isActive")
                }
            }
        }

        // TODO: 28.11.2021 43.2. Coroutine Builders
        val scope7 = CoroutineScope(coroutineException)
        scope7.launch {
            try {
                coroutineScope {
                    val sum = 10 / 0
                }
                val sum = mCoroutineScope()
            } catch (error: ArithmeticException) {
                error.printStackTrace()
            }
            val sum = mSupervisorScope()


        }

        val scope8 = CoroutineScope(SupervisorJob() + EmptyCoroutineContext)
        scope8.launch {
            mWithContext()
            mRunBlock()
        }

    }

    private suspend fun mCoroutineScope(): Int {
        return coroutineScope {
            val sum = 10 / 0
            return@coroutineScope sum
        }
    }

    private suspend fun mSupervisorScope(): Int {
        return supervisorScope {
            val job = async(coroutineException) {
                val sum = 10 / 0
                return@async sum
            }
            return@supervisorScope job.await()
        }
    }

    private suspend fun mWithContext() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(TimeUnit.SECONDS.toMillis(1L))
            print("!!!!! ${contextToString(coroutineContext)}")
            withContext(Dispatchers.Main) {
                print("!!!!! ${contextToString(coroutineContext)}")
            }
        }
    }

    private fun mRunBlock() {
        runBlocking {
            val time = measureTimeMillis {
                runBlocking {
                    launch {
                        delay(2000)
                    }
                }
            }
            print(time)
        }
    }

    fun cancelSelectCat() {
        //job.cancel()
        //scope2.cancel()
        scope6.cancel()
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

    // TODO: 29.11.2021 Channels
    fun channels() {

        // TODO: 29.11.2021 Rendezvous Channel 
        val channel1 = Channel<Int>()
        CoroutineScope(EmptyCoroutineContext).launch {
            launch {
                println("start sending")
                channel1.send(1)
                channel1.send(2)
                channel1.close()
                println("stop sending")
            }
            launch {
                println("star receiving")
                for (result in channel1) {
                    println("result: $result")
                }
                println("stop receiving")
            }
        }

        // TODO: 29.11.2021 Conflated and Buffered Channel
        val channel2 = Channel<Int>(Channel.BUFFERED)
        CoroutineScope(EmptyCoroutineContext).launch {
            this.coroutineContext[Job]?.invokeOnCompletion { channel2.close() }

            launch {
                repeat(8) { action ->
                    delay(TimeUnit.MILLISECONDS.toMillis(200L))
                    channel2.send(action)
                    println("send1 $action")
                }
                println("send1 end")
            }

            launch {
                repeat(8) { action ->
                    delay(TimeUnit.MILLISECONDS.toMillis(200L))
                    channel2.send(action)
                    println("send2 $action")
                }
                println("send2 end")
            }
        }

        CoroutineScope(EmptyCoroutineContext).launch {
            for (result in channel2) {
                delay(TimeUnit.SECONDS.toMillis(1L))
                println("received $result")
            }
            println("received end")
        }

        // TODO: 29.11.2021 BroadCast Channel
        val broadcastChannel = BroadcastChannel<Int>(Channel.BUFFERED)
        CoroutineScope(EmptyCoroutineContext).launch {
            repeat(5) {
                delay(TimeUnit.SECONDS.toMillis(1L))
                broadcastChannel.send(it)
                println("broadcast send $it")
            }
            println("broadcast send close")
        }.invokeOnCompletion { broadcastChannel.close() }

        CoroutineScope(EmptyCoroutineContext).launch {
            launch {
                for (result in broadcastChannel.openSubscription()) {
                    println("broadcast received1 $result ")
                }
                println("broadcast received1 close")
            }

            launch {
                for (result in broadcastChannel.openSubscription()) {
                    println("broadcast received2 $result ")
                }
                println("broadcast received2 close")
            }

        }
        // TODO: 29.11.2021 Flow
        runFlow()
        CoroutineScope(EmptyCoroutineContext).launch {
            emitterFlow(1, 2).collect {
                println("emitterFlow $it")
            }
        }

    }


    // TODO: 29.11.2021 Flow
    private fun runFlow() {
        val function = emitter("test 1")
        function.invoke {
            println("emitter $it")
        }
    }

    private fun emitter(s: String): ((String) -> Unit) -> Unit {
        return { emitter ->
            repeat(5) {
                emitter.invoke(s)
            }
        }
    }

    private fun emitterFlow(s: Int, multiplier: Int): Flow<String> {
        return flow {
            repeat(5) {
                emit(s + it)
            }
        }.map { (it * multiplier).toString() }
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