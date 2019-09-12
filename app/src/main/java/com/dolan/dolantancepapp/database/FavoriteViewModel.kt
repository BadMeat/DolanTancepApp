package com.dolan.dolantancepapp.database

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.select

class FavoriteViewModel : ViewModel() {

    private val favoriteList: MutableLiveData<MutableList<Favorite>> = MutableLiveData()

    fun getFavoriteData(ctx: Context) {
        val itemList = mutableListOf<Favorite>()
        ctx.database.use {
            val select = select(Favorite.TABLE_NAME)
            val result = select.parseList(classParser<Favorite>())
            itemList.addAll(result)
        }
        favoriteList.postValue(itemList)
    }

    fun getFavoriteData() = favoriteList

    fun deleteFavoriteData(db: DatabaseOpenHelper?, id: Int) {
        db?.use {
            delete(
                Favorite.TABLE_NAME, "${Favorite.FAV_ID} = {id}",
                "id" to id
            )
        }
    }

}