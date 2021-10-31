package com.w4eret1ckrtb1tch.app38.db.room

import android.os.Parcelable
import androidx.room.*
import com.w4eret1ckrtb1tch.app38.db.room.RoomScheme.DataBase.TableBed
import com.w4eret1ckrtb1tch.app38.db.room.RoomScheme.DataBase.TableCat

import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = TableCat.NAME,
    indices = [Index(
        value = [TableCat.Column.NAME],
        unique = false
    )]
)
data class CatEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = TableCat.Column.NAME, typeAffinity = ColumnInfo.TEXT) val name: String,
    @ColumnInfo(name = TableCat.Column.COLOR, defaultValue = "0") val color: Int = 0,
    @ColumnInfo(name = TableCat.Column.AGE) val age: Int = 0,
    @Ignore val isLive: Boolean = true
) : Parcelable

@Parcelize
@Entity(
    tableName = TableBed.NAME,
    foreignKeys = [ForeignKey(
        entity = CatEntity::class,
        parentColumns = [TableCat.Column.ID],
        childColumns = [TableBed.Column.ID_CAT],
        onDelete = ForeignKey.CASCADE
    )]
)
data class BedEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = TableBed.Column.MODEL) val model: String,
    @ColumnInfo(name = TableBed.Column.ID_CAT) val idCat: Long
) : Parcelable