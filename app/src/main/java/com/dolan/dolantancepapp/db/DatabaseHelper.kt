package com.dolan.dolantancepapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.DATE
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.POSTER
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.RATE
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.TABLE_FAV
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.TITLE
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.TYPE

class DatabaseHelper(ctx: Context?) : SQLiteOpenHelper(ctx, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DB_QUERY_FAV)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FAV")
    }

    companion object {
        const val DB_NAME = "favdb"
        const val DB_VERSION = 1

        const val DB_QUERY_FAV = "CREATE TABLE $TABLE_FAV(" +
                "$_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$TITLE TEXT," +
                "$DATE TEXT," +
                "$RATE REAL," +
                "$POSTER TEXT," +
                "$TYPE INTEGER" +
                ")"
    }

}