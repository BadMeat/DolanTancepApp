package com.dolan.dolantancepapp.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns._ID
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.TABLE_FAV

class FavoriteHelper(ctx: Context?) {

    private var databaseHelper: DatabaseHelper? = DatabaseHelper(ctx)
    private var database: SQLiteDatabase? = null

    companion object {
        var instance: FavoriteHelper? = null

        fun getInstance(ctx: Context?): FavoriteHelper {
            if (instance == null) {
                synchronized(FavoriteHelper::class) {
                    instance = FavoriteHelper(ctx)
                }
            }
            return instance as FavoriteHelper
        }
    }

    fun open() {
        database = databaseHelper?.writableDatabase
    }

    fun close() {
        databaseHelper?.close()
        database?.close()
    }

    fun getProviderById(id: String?): Cursor? {
        return database?.query(
            TABLE_FAV,
            null,
            "$_ID = ?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
    }

    fun getProvider(): Cursor? {
        return database?.query(
            TABLE_FAV,
            null,
            null,
            null,
            null,
            null,
            "$_ID DESC"
        )
    }

    fun insertProvider(values: ContentValues?): Long? {
        return database?.insert(TABLE_FAV, null, values)
    }

    fun updateProvider(id: String?, values: ContentValues?): Int? {
        return database?.update(TABLE_FAV, values, "$_ID = ?", arrayOf(id))
    }

    fun deleteProvider(id: String?): Int? {
        return database?.delete(TABLE_FAV, "$_ID = ?", arrayOf(id))
    }

}