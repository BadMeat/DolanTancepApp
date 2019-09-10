package com.dolan.dolantancepapp.network

import com.dolan.dolantancepapp.BuildConfig
import com.dolan.dolantancepapp.tv.TvResponse
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    private var data: GetDataService

    companion object {
        private var service: ApiClient? = null
        val instance: ApiClient
            get() {
                if (service == null) {
                    service = ApiClient()
                }
                return service as ApiClient
            }
    }

    init {
        val retro = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()

        data = retro.create(GetDataService::class.java)
    }

    fun getTvSearch(language: String?, title: String): Observable<Response<TvResponse>> {
        return data.getMovieList(language, title)
    }

}