package com.dolan.dolantancepapp.db

import android.database.Cursor

interface LoadFavCallback {
    fun preExecute()
    fun postExecute(cursor : Cursor?)
}