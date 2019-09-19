package com.dolan.dolantancepapp.db

import android.database.Cursor
import android.os.Parcelable
import android.provider.BaseColumns._ID
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.DATE
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.POSTER
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.RATE
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.TITLE
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite(
    var id: Int? = 0,
    var title: String? = "TITLE",
    var date: String? = "DATE",
    var rate: Double? = 0.0,
    var poster: String? = "POSTER"
) : Parcelable {
    constructor(cursor: Cursor?) : this() {
        id = cursor?.getInt(cursor.getColumnIndexOrThrow(_ID))
        title = cursor?.getString(cursor.getColumnIndexOrThrow(TITLE))
        date = cursor?.getString(cursor.getColumnIndexOrThrow(DATE))
        rate = cursor?.getDouble(cursor.getColumnIndexOrThrow(RATE))
        poster = cursor?.getString(cursor.getColumnIndexOrThrow(POSTER))
    }
}