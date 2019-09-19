package com.dolan.dolantancepapp.tv

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dolan.dolantancepapp.R
import com.dolan.dolantancepapp.database.FavoriteTemp
import com.dolan.dolantancepapp.database.database
import com.dolan.dolantancepapp.favorite.StackWidgetService
import com.dolan.dolantancepapp.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.db.insert

class TvViewModel : ViewModel() {

    private val listTv: MutableLiveData<MutableList<ResultsItem?>> = MutableLiveData()

    private var disposable: Disposable? = null

    fun insertFav(context: Context?, it: ResultsItem) {
        try {
            context?.database?.use {
                insert(
                    FavoriteTemp.TABLE_NAME,
                    FavoriteTemp.FAV_ID to it.id,
                    FavoriteTemp.FAV_TITLE to it.name,
                    FavoriteTemp.FAV_RATE to it.voteAverage,
                    FavoriteTemp.FAV_DATE to it.firstAirDate,
                    FavoriteTemp.FAV_POSTER to it.posterPath
                )
            }
            Toast.makeText(
                context,
                context?.resources?.getString(R.string.save_succes),
                Toast.LENGTH_SHORT
            ).show()
//            updateWidget(context)
        } catch (e: Exception) {
            Log.e("Error", "Save Failed")
        }
    }

    fun getTv(language: String, title: String) {
        val item = mutableListOf<ResultsItem?>()
        disposable = ApiClient.instance.getTvSearch(language, title)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map {
                it.body()?.results
            }
            .doFinally {
                listTv.postValue(item)
            }
            .subscribe(
                { result ->
                    if (result != null) {
                        item.addAll(result.toMutableList())
                    }
                },
                { error -> Log.e("Error Response", "$error") }
            )
    }

    private fun updateWidget(context: Context?) {
        val intent = Intent(context, StackWidgetService::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(context)
            .getAppWidgetIds(ComponentName(context!!, StackWidgetService::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        context.sendBroadcast(intent)

//        val appWidgetManager = AppWidgetManager.getInstance(context)
//        val remote = RemoteViews(context?.packageName, R.layout.favorite_widget)
//        val thisWidget = ComponentName(context!!, FavoriteWidget::class.java)
//        appWidgetManager.updateAppWidget(thisWidget, remote)
    }

    fun getTvList() = listTv
}