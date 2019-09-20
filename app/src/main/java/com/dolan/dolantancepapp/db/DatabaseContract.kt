package com.dolan.dolantancepapp.db

import android.database.Cursor
import android.net.Uri

class DatabaseContract {

    companion object {

        const val TABLE_FAV = "TABLE_FAV"
        const val TITLE = "FAV_TITLE"
        const val DATE = "FAV_DATE"
        const val RATE = "FAV_RATE"
        const val POSTER = "FAV_POSTER"


        const val AUTH = "com.dolan.dolantancepapp"
        private const val SCHEMA = "content"
        var CONTENT_URI: Uri = Uri.Builder()
            .authority(AUTH)
            .scheme(SCHEMA)
            .appendPath(TABLE_FAV)
            .build()

        fun getColumnInt(cursor: Cursor?, columnName: String?): Int? {
            return cursor?.getInt(cursor.getColumnIndexOrThrow(columnName))
        }

        fun getColumnString(cursor: Cursor?, columnName: String?): String? {
            return cursor?.getString(cursor.getColumnIndexOrThrow(columnName))
        }
    }
}