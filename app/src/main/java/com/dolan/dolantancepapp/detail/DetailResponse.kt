package com.dolan.dolantancepapp.detail

import com.google.gson.annotations.SerializedName

data class DetailResponse(
	val id: Int? = null,
	@SerializedName("first_air_date")
	val firstAirDate: String? = null,
	val overview: String? = null,
	val languages: List<String?>? = null,
	@SerializedName("poster_path")
	val posterPath: String? = null,
	@SerializedName("vote_average")
	val voteAverage: Double? = null,
	val name: String? = null
)
