package com.w4eret1ckrtb1tch.app38.db

import android.content.ContentValues
import android.content.Context
import com.w4eret1ckrtb1tch.app38.db.DbScheme.Table

class DataBase private constructor(context: Context) {

    private val applicationContext = context.applicationContext
    private val sqliteDb = DbHelper(applicationContext).writableDatabase

    companion object {
        private var dataBase: DataBase? = null
        fun getDataBase(context: Context): DataBase? {
            if (dataBase == null) dataBase = DataBase(context)
            return dataBase
        }
    }

    private fun getContentValue(item: Item): ContentValues {
        val values = ContentValues()
        values.put(Table.Column.NAME, item.name)
        values.put(Table.Column.PRICE, item.price)
        return values
    }

    fun addItem(item: Item) {
        val values = getContentValue(item)
        sqliteDb.insert(Table.NAME, null, values)
    }

}