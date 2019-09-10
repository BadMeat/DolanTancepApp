package com.dolan.dolantancepapp.database

data class Favorite(
    val id: Int?,
    val title: String?,
    val date: String?,
    val rate: Double?,
    val poster: String?
) {
    companion object {
        const val TABLE_NAME = "TABLE_FAVORITE"
        const val FAV_TITLE = "FAV_TITLE"
        const val FAV_DATE = "FAV_DATE"
        const val FAV_RATE = "FAV_RATE"
        const val FAV_POSTER = "FAV_POSTER"
    }
}