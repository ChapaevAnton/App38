package com.w4eret1ckrtb1tch.app38.db.room

import androidx.room.*

@Dao
interface CatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cats: List<CatEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCat(cat: CatEntity)

    @Query("SELECT * FROM cat_table")
    fun selectAll(): List<CatEntity>

    @Query("SELECT * FROM cat_table WHERE id = :id")
    fun selectCat(id: Long): List<CatEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBed(bed: BedEntity)

    @Transaction
    fun insert(cat: CatEntity, bed: BedEntity) {
        insertCat(cat)
        insertBed(bed)
    }
}