package com.dolan.dolantancepapp.movie

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dolan.dolantancepapp.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MovieViewModel : ViewModel() {

    private val movieList = MutableLiveData<MutableList<Movie?>>()
    private var disposable: Disposable? = null

    fun getData(language: String?, title: String?) {

        val movie = mutableListOf<Movie?>()

        disposable = ApiClient.instance.getMovieSearch(language, title)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map {
                it.body()?.results
            }
            .doFinally {
                movieList.postValue(movie)
            }
            .subscribe(
                { result ->
                    if (result != null) {
                        movie.addAll(result)
                    }
                },
                { error -> Log.e("Eror Rquest Movie", "$error") }
            )
    }

    fun closeDispose() {
        disposable?.dispose()
    }

    fun getMovieList() = movieList

}