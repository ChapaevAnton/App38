package com.w4eret1ckrtb1tch.app38.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [CatEntity::class, BedEntity::class], version = 2, exportSchema = true)
@TypeConverters(TypeConverter::class)
abstract class CatDataBase : RoomDatabase() {

    abstract fun catDao(): CatDao

    companion object {
        private var instance: CatDataBase? = null

        fun getCatDataBase(context: Context): CatDataBase {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        CatDataBase::class.java,
                        RoomScheme.DataBase.NAME
                    ).addMigrations(CatMigrationFromV1_2)
                        .allowMainThreadQueries()
                        .build()
            }
            return instance as CatDataBase
        }
    }
}