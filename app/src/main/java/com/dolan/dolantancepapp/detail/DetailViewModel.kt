package com.dolan.dolantancepapp.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dolan.dolantancepapp.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailViewModel : ViewModel() {

    private val detailTvList: MutableLiveData<DetailTv> = MutableLiveData()
    private val detailMovieList: MutableLiveData<DetailMovie> = MutableLiveData()

    private var disposable: Disposable? = null

    fun getTvData(id: Int, language: String?) {

        val itemList = mutableListOf<DetailTv>()

        disposable = ApiClient.instance.getTvDetail(id, language)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map {
                it.body()
            }.doFinally {
                if (itemList.isNotEmpty()) {
                    detailTvList.postValue(itemList[0])
                }
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

    fun getMovieData(id: Int, language: String?) {

        val itemList = mutableListOf<DetailMovie>()

        disposable = ApiClient.instance.getMovieDetail(id, language)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map {
                it.body()
            }.doFinally {
                if (itemList.isNotEmpty()) {
                    detailMovieList.postValue(itemList[0])
                }
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

    fun getTvDetailList() = detailTvList

    fun getMovieDetailList() = detailMovieList

    fun close() {
        disposable?.dispose()
    }
}