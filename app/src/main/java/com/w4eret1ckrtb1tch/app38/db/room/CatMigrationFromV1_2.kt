package com.w4eret1ckrtb1tch.app38.db.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object CatMigrationFromV1_2 : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """ALTER TABLE ${RoomScheme.DataBase.TableCat.NAME}
            |ADD COLUMN ${RoomScheme.DataBase.TableCat.Column.FAVORITE_FOOD} TEXT NOT NULL DEFAULT ""
            """.trimMargin()
        )
    }
}