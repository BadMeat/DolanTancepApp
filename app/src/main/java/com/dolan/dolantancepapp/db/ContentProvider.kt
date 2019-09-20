package com.dolan.dolantancepapp.db

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import com.dolan.dolantancepapp.FavoriteFragment
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.AUTH
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.CONTENT_URI
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.TABLE_FAV

class ContentProvider : ContentProvider() {

    private var favHelper: FavoriteHelper? = null

    companion object {
        const val FAV = 1
        const val FAV_ID = 2
        private var uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(AUTH, TABLE_FAV, FAV)
            uriMatcher.addURI(AUTH, "$TABLE_FAV/#", FAV_ID)
        }
    }

    override fun onCreate(): Boolean {
        favHelper = FavoriteHelper.getInstance(context)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        favHelper?.open()
        var cursor: Cursor? = null
        when (uriMatcher.match(uri)) {
            FAV -> {
                cursor = favHelper?.getProvider()
            }
            FAV_ID -> {
                cursor = favHelper?.getProviderById(uri.lastPathSegment)
            }
        }
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        favHelper?.open()
        val added = when (uriMatcher.match(uri)) {
            FAV -> {
                favHelper?.insertProvider(values)
            }
            else -> {
                0
            }
        }
        context?.contentResolver?.notifyChange(
            CONTENT_URI, null
        )
        return Uri.parse("$CONTENT_URI/$added")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        favHelper?.open()
        val updated = when (uriMatcher.match(uri)) {
            FAV_ID -> {
                favHelper?.updateProvider(uri.lastPathSegment, values)
            }
            else -> {
                0
            }
        }
        context?.contentResolver?.notifyChange(
            CONTENT_URI, FavoriteFragment.DataObserver(Handler(), context)
        )
        return updated!!
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        favHelper?.open()
        val deleted = when (uriMatcher.match(uri)) {
            FAV_ID -> {
                favHelper?.deleteProvider(uri.lastPathSegment)
            }
            else -> {
                0
            }
        }
        context?.contentResolver?.notifyChange(
            CONTENT_URI, FavoriteFragment.DataObserver(Handler(), context)
        )
        return deleted!!
    }

    override fun getType(uri: Uri) = null
}