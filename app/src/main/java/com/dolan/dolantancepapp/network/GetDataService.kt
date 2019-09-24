package com.dolan.dolantancepapp.network

import com.dolan.dolantancepapp.BuildConfig
import com.dolan.dolantancepapp.alarm.ResponseTvRelase
import com.dolan.dolantancepapp.detail.DetailMovie
import com.dolan.dolantancepapp.detail.DetailTv
import com.dolan.dolantancepapp.movie.ResponseMovie
import com.dolan.dolantancepapp.tv.TvResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetDataService {

    /**
     * TV
     */
    @GET("${BuildConfig.BASE_URL}search/tv?api_key=${BuildConfig.API_KEY}")
    fun getTvList(
        @Query("language") language: String? = "en-US",
        @Query("query") title: String?
    ): Observable<Response<TvResponse>>

    @GET("${BuildConfig.BASE_URL}tv/{id}?api_key=${BuildConfig.API_KEY}")
    fun getTvDetail(
        @Path("id", encoded = true) id: Int,
        @Query("language") language: String? = "en-US"
    ): Observable<Response<DetailTv>>

    @GET("${BuildConfig.BASE_URL}discover/tv?api_key=${BuildConfig.API_KEY}")
    fun getTvRelease(
        @Query("primary_release_date.gte") todayGte: String?,
        @Query("primary_release_date.lte") todayLte: String?
    ): Observable<Response<ResponseTvRelase>>

    /**
     * Movie
     */
    @GET("${BuildConfig.BASE_URL}search/movie?api_key=${BuildConfig.API_KEY}")
    fun getMovieList(
        @Query("language") language: String? = "en-US",
        @Query("query") title: String?
    ) : Observable<Response<ResponseMovie>>

    @GET("${BuildConfig.BASE_URL}movie/{id}?api_key=${BuildConfig.API_KEY}")
    fun getMovieDetail(
        @Path("id", encoded = true) id: Int,
        @Query("language") language: String? = "en-US"
    ): Observable<Response<DetailMovie>>
}