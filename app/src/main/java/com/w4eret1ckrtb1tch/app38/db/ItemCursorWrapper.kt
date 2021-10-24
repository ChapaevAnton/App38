package com.w4eret1ckrtb1tch.app38.db

import android.database.Cursor
import android.database.CursorWrapper
import com.w4eret1ckrtb1tch.app38.db.DbScheme.Table

class ItemCursorWrapper(cursor: Cursor) : CursorWrapper(cursor) {

    fun getItem(): Item {
        val name = getString(getColumnIndex(Table.Column.NAME))
        val price = getDouble(getColumnIndex(Table.Column.PRICE))
        return Item(name, price)
    }
}