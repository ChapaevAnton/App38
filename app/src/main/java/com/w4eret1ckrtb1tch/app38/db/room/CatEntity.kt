package com.w4eret1ckrtb1tch.app38.db.room

import android.os.Parcelable
import androidx.room.*
import com.w4eret1ckrtb1tch.app38.db.room.RoomScheme.DataBase.EmbeddedAddress
import com.w4eret1ckrtb1tch.app38.db.room.RoomScheme.DataBase.TableBed
import com.w4eret1ckrtb1tch.app38.db.room.RoomScheme.DataBase.TableCat
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.*


@Parcelize
@Entity(
    tableName = TableCat.NAME,
    indices = [Index(
        value = [TableCat.Column.NAME],
        unique = false
    )]
)
data class CatEntity constructor(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = TableCat.Column.NAME, typeAffinity = ColumnInfo.TEXT) val name: String,
    @ColumnInfo(name = TableCat.Column.COLOR, defaultValue = "0") val color: Int = 0,
    @ColumnInfo(name = TableCat.Column.BIRTH_DAY) val birthDay: Date? = null,
    @ColumnInfo(name = TableCat.Column.AGE) val age: Int = 0,
    @Embedded val address: Address? = null
) : Parcelable {
    @IgnoredOnParcel
    @Ignore
    val isLive: Boolean = true
}

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
    @ColumnInfo(name = TableBed.Column.ID_CAT) val idCat: Long = 0
) : Parcelable

@Parcelize
data class Address(
    @ColumnInfo(name = EmbeddedAddress.CITY) val city: String,
    @ColumnInfo(name = EmbeddedAddress.STREET) val street: String,
    @ColumnInfo(name = EmbeddedAddress.HOUSE_NUMBER) val houseNumber: String
) : Parcelable