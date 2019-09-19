package com.dolan.dolantancepapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dolan.dolantancepapp.database.FavoriteTemp.Companion.FAV_DATE
import com.dolan.dolantancepapp.database.FavoriteTemp.Companion.FAV_ID
import com.dolan.dolantancepapp.database.FavoriteTemp.Companion.FAV_POSTER
import com.dolan.dolantancepapp.database.FavoriteTemp.Companion.FAV_RATE
import com.dolan.dolantancepapp.database.FavoriteTemp.Companion.FAV_TITLE
import com.dolan.dolantancepapp.database.FavoriteTemp.Companion.TABLE_NAME

class DatabaseHelper(ctx: Context?) : SQLiteOpenHelper(ctx, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DB_QUERY_FAV)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }

    companion object {
        const val DB_NAME = "favdb"
        const val DB_VERSION = 1

        const val DB_QUERY_FAV = "CREATE TABLE $TABLE_NAME(" +
                "$FAV_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$FAV_TITLE TEXT," +
                "$FAV_DATE TEXT," +
                "$FAV_RATE REAL," +
                "$FAV_POSTER TEXT" +
                ")"
    }

}