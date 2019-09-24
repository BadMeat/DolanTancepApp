package com.dolan.dolantancepapp.db

import android.database.Cursor
import android.os.Parcelable
import android.provider.BaseColumns._ID
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.DATE
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.POSTER
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.RATE
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.TITLE
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.TYPE
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite(
    var id: Int? = 0,
    var title: String? = "TITLE",
    var date: String? = "DATE",
    var rate: Double? = 0.0,
    var poster: String? = "POSTER",
    var type: Int? = 1
) : Parcelable {
    constructor(cursor: Cursor?) : this() {
        id = DatabaseContract.getColumnInt(cursor, _ID)
        title = DatabaseContract.getColumnString(cursor, TITLE)
        date = DatabaseContract.getColumnString(cursor, DATE)
        rate = DatabaseContract.getColumnDouble(cursor, RATE)
        poster = DatabaseContract.getColumnString(cursor, POSTER)
        type = DatabaseContract.getColumnInt(cursor, TYPE)
    }
}