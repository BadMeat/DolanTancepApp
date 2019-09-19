package com.dolan.dolantancepapp.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dolan.dolantancepapp.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailViewModel : ViewModel() {

    private val detailList: MutableLiveData<DetailResponse> = MutableLiveData()
    private var disposable: Disposable? = null

    fun getData(id: Int, language: String?) {

        val itemList = mutableListOf<DetailResponse>()

        disposable = ApiClient.instance.getTvDetail(id, language)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map {
                it.body()
            }.doFinally {
                detailList.postValue(itemList[0])
            }
            .subscribe(
                { result ->
                    if (result != null) {
                        itemList.add(result)
                    }
                },
                { error -> Log.d("DetailViewModel", "$error") }
            )
    }

    fun getDetailList() = detailList

    fun close() {
        disposable?.dispose()
    }
}