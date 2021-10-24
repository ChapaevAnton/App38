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

    private fun queryItems(whereClause: String?, whereArgs: Array<String>?): ItemCursorWrapper {
        val cursor = sqliteDb.query(
            Table.NAME,
            null,
            whereClause,
            whereArgs,
            null,
            null,
            null
        )
        return ItemCursorWrapper(cursor)
    }

    fun addItem(item: Item) {
        val values = getContentValue(item)
        sqliteDb.insert(Table.NAME, null, values)
    }

    fun updateItem(item: Item) {
        val name = item.name
        val values = getContentValue(item)
        val whereClause = "${Table.Column.NAME} = ?"
        val whereArgs = arrayOf(name)
        sqliteDb.update(Table.NAME, values, whereClause, whereArgs)
    }

    fun deleteItem(item: Item) {
        val name = item.name
        val whereClause = "${Table.Column.NAME} = ?"
        val whereArgs = arrayOf(name)
        sqliteDb.delete(Table.NAME, whereClause, whereArgs)
    }

    fun getItem(name: String): Item? {
        val whereClause = "${Table.Column.NAME} = ?"
        val whereArgs = arrayOf(name)
        val cursor = queryItems(whereClause, whereArgs)
        cursor.use { table ->
            if (table.count == 0) return null
            table.moveToFirst()
            return table.getItem()
        }
    }

    fun getItems(): List<Item> {
        val items = mutableListOf<Item>()
        val cursor = queryItems(null, null)
        cursor.use { table ->
            table.moveToFirst()
            while (!table.isAfterLast) {
                items.add(table.getItem())
                table.moveToNext()
            }
        }
        return items
    }

}