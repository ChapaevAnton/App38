package com.w4eret1ckrtb1tch.app38.db.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cats: List<CatEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(cat: CatEntity)

    @Query("SELECT * FROM cat_table")
    fun selectAll(): List<CatEntity>

}