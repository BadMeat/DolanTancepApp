package com.dolan.dolantancepapp

import android.view.View
import java.text.SimpleDateFormat
import java.util.*

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun getConvertDate(dateString: String?): String? {
    if (!dateString.isNullOrBlank()) {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
        val monthDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return monthDate.format(date)
    }
    return ""
}

fun getLanguage() : String{
    val defaultLang = Locale.getDefault().displayLanguage
    if (defaultLang.equals("Indonesia", true)) {
        return "id"
    }
    return "en-US"
}