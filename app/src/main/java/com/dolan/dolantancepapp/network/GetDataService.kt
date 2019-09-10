package com.dolan.dolantancepapp.network

import com.dolan.dolantancepapp.BuildConfig
import com.dolan.dolantancepapp.tv.TvResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GetDataService {

    @GET("${BuildConfig.BASE_URL}search/tv?api_key=${BuildConfig.API_KEY}")
    fun getMovieList(
        @Query("language") language: String? = "en-US",
        @Query("query") title: String?
    ): Observable<Response<TvResponse>>
}