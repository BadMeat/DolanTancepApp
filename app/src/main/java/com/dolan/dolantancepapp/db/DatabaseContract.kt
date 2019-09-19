package com.dolan.dolantancepapp.db

import android.net.Uri

class DatabaseContract {

    companion object {

        const val TABLE_FAV = "TABLE_FAV"
        const val TITLE = "FAV_TITLE"
        const val DATE = "FAV_DATE"
        const val RATE = "FAV_RATE"
        const val POSTER = "FAV_POSTER"


        const val AUTH = "com.dolan.dolantancepapp"
        const val SCHEMA = "content"
        var CONTENT_URI = Uri.Builder()
            .authority(AUTH)
            .scheme(SCHEMA)
            .appendPath(TABLE_FAV)
            .build()
    }
}