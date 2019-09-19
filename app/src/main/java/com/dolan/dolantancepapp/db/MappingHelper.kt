package com.dolan.dolantancepapp.db

import android.database.Cursor
import android.provider.BaseColumns._ID
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.DATE
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.POSTER
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.RATE
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.TITLE

class MappingHelper {

    companion object {

        fun mapCursorToArrayList(cursor: Cursor?): List<Favorite> {
            val favList = mutableListOf<Favorite>()
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE))
                    val date = cursor.getString(cursor.getColumnIndexOrThrow(DATE))
                    val rate = cursor.getDouble(cursor.getColumnIndexOrThrow(RATE))
                    val poster = cursor.getString(cursor.getColumnIndexOrThrow(POSTER))
                    val fav = Favorite(id, title, date, rate, poster)
                    favList.add(fav)
                }
            }
            return favList
        }
    }
}