package com.w4eret1ckrtb1tch.app38.db.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.w4eret1ckrtb1tch.app38.db.sqlite.DbScheme.Table

class DbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "prices.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            """CREATE TABLE ${Table.NAME} (
            |${Table.Column.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            |${Table.Column.NAME} TEXT,
            |${Table.Column.PRICE} REAL)
            |""".trimMargin()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            db?.execSQL("""DROP TABlE ${Table.NAME}""")
            onCreate(db)
        }
    }
}