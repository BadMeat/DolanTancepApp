package com.dolan.dolantancepapp.database

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dolan.dolantancepapp.db.Favorite
import org.jetbrains.anko.db.delete

class FavoriteViewModel : ViewModel() {

    private val favoriteList: MutableLiveData<MutableList<Favorite>> = MutableLiveData()

    fun getFavoriteData(ctx: Context) {
        val itemList = mutableListOf<Favorite>()

        favoriteList.postValue(itemList)
    }

    fun getFavoriteData() = favoriteList

    fun deleteFavoriteData(db: DatabaseOpenHelper?, id: Int) {
        db?.use {
            delete(
                FavoriteTemp.TABLE_NAME, "${FavoriteTemp.FAV_ID} = {id}",
                "id" to id
            )
        }
    }

}