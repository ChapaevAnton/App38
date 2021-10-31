package com.w4eret1ckrtb1tch.app38.db.room

class RoomScheme {

    object DataBase {
        const val NAME = "cats_database"

        object TableCat {

            const val NAME = "cat_table"

            object Column {
                const val ID = "id"
                const val NAME = "name"
                const val COLOR = "color"
                const val AGE = "age"
            }
        }

        object TableBed {
            const val NAME = "bed_table"

            object Column {
                const val ID_CAT = "id_cat"
                const val MODEL = "model"
            }
        }
    }

}