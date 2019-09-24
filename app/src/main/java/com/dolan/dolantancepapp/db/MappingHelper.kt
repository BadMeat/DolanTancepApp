package com.dolan.dolantancepapp.db

import android.database.Cursor

class MappingHelper {

    companion object {

        fun mapCursorToArrayList(cursor: Cursor?): List<Favorite> {
            val favList = mutableListOf<Favorite>()
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val fav = Favorite(cursor)
                    favList.add(fav)
                }
            }
            return favList
        }
    }
}