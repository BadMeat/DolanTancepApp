package com.dolan.dolantancepapp.favorite

import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.os.AsyncTask
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dolan.dolantancepapp.db.DatabaseContract
import com.dolan.dolantancepapp.db.Favorite
import com.dolan.dolantancepapp.db.LoadFavCallback
import com.dolan.dolantancepapp.db.MappingHelper
import java.lang.ref.WeakReference

/**
 * Created by Bencoleng on 05/10/2019.
 */
class FavoriteViewModel : ViewModel(), LoadFavCallback {



    private val favList = MutableLiveData<List<Favorite>>()

    override fun preExecute() {

    }

    override fun postExecute(cursor: Cursor?) {
        if (cursor != null) {
            val listFav = MappingHelper.mapCursorToArrayList(cursor)
            if (listFav.isNotEmpty()) {
                favList.postValue(listFav)
            }
        }
    }

    fun getData(context : Context){
        LoadFavAsyn(context, this).execute()
    }

    fun getFavList() = favList

    class LoadFavAsyn(
        ctx: Context,
        loadCallBack: LoadFavCallback
    ) : AsyncTask<Void, Void, Cursor?>() {

        private val weakContext = WeakReference(ctx)
        private val weakLoadCallBack = WeakReference(loadCallBack)

        override fun onPreExecute() {
            super.onPreExecute()
            weakLoadCallBack.get()?.preExecute()
        }

        override fun doInBackground(vararg params: Void?): Cursor? {
            val contexte = weakContext.get()
            return contexte?.contentResolver?.query(DatabaseContract.CONTENT_URI, null, null, null, null)
        }

        override fun onPostExecute(result: Cursor?) {
            super.onPostExecute(result)
            weakLoadCallBack.get()?.postExecute(result)
        }
    }

    class DataObserver(handler: Handler, ctx: Context?) : ContentObserver(handler) {
        val context = ctx

        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            if (context != null) {
                val fragment: LoadFavCallback? = context as LoadFavCallback
                if (fragment != null) {
                    LoadFavAsyn(
                        context,
                        fragment
                    ).execute()
                }
            }
        }
    }

}