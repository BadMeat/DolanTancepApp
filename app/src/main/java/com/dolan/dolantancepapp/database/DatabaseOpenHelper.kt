package com.dolan.dolantancepapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class DatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "favorite.db", null) {

    companion object {
        private var instance: DatabaseOpenHelper? = null

        fun getInstance(ctx: Context): DatabaseOpenHelper {
            if (instance == null) {
                instance = DatabaseOpenHelper(ctx)
            }
            return instance as DatabaseOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(
            FavoriteTemp.TABLE_NAME, true,
            FavoriteTemp.FAV_ID to INTEGER + PRIMARY_KEY,
            FavoriteTemp.FAV_TITLE to TEXT,
            FavoriteTemp.FAV_DATE to TEXT,
            FavoriteTemp.FAV_RATE to REAL,
            FavoriteTemp.FAV_POSTER to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(FavoriteTemp.TABLE_NAME)
    }
}

val Context.database: DatabaseOpenHelper
    get() = DatabaseOpenHelper.getInstance(applicationContext)