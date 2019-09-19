package com.dolan.dolantancepapp.detail

import com.google.gson.annotations.SerializedName

data class DetailResponse(
	val originalLanguage: String? = null,
	val numberOfEpisodes: Int? = null,
	val type: String? = null,
	val backdropPath: String? = null,
	val popularity: Double? = null,
	val id: Int? = null,
	val numberOfSeasons: Int? = null,
	val voteCount: Int? = null,
	@SerializedName("first_air_date")
	val firstAirDate: String? = null,
	val overview: String? = null,
	val languages: List<String?>? = null,
	val posterPath: String? = null,
	val originCountry: List<String?>? = null,
	val originalName: String? = null,
	@SerializedName("vote_average")
	val voteAverage: Double? = null,
	val name: String? = null,
	val episodeRunTime: List<Int?>? = null,
	val inProduction: Boolean? = null,
	val lastAirDate: String? = null,
	val homepage: String? = null,
	val status: String? = null
)
