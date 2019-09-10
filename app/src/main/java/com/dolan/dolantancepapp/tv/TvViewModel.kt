package com.dolan.dolantancepapp.tv

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dolan.dolantancepapp.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TvViewModel : ViewModel() {

    private val listTv: MutableLiveData<MutableList<ResultsItem?>> = MutableLiveData()

    private var disposable: Disposable? = null

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
                    Log.d("Respnse", "$result")
                },
                { error -> Log.e("Error Response", "$error") }
            )
    }

    fun getTvList() = listTv
}