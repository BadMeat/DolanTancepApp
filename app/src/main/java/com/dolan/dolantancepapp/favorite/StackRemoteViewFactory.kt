package com.dolan.dolantancepapp.favorite

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.dolan.dolantancepapp.BuildConfig
import com.dolan.dolantancepapp.R
import com.dolan.dolantancepapp.database.Favorite
import com.dolan.dolantancepapp.database.database
import com.squareup.picasso.Picasso
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

class StackRemoteViewFactory(ctx: Context) : RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItem = ArrayList<Bitmap>()
    private val faList = mutableListOf<Favorite>()
    private var context: Context? = null

    init {
        context = ctx
    }

    override fun onCreate() {
    }

    override fun getLoadingView() = null

    override fun getItemId(position: Int) = 0L

    override fun onDataSetChanged() {

        val token = Binder.clearCallingIdentity()

        context?.database?.use {
            val select = select(Favorite.TABLE_NAME)
            val parse = select.parseList(classParser<Favorite>())
            faList.addAll(parse)
        }

        Log.d(StackRemoteViewFactory::class.java.simpleName, "Update lhoo")

        for (i: Favorite in faList) {
            if (i.poster?.trim() != null) {
                val bitmap = Picasso.get().load("${BuildConfig.BASE_IMAGE}${i.poster}").get()
                mWidgetItem.add(bitmap)
            }
        }

        Binder.restoreCallingIdentity(token)
    }

    override fun hasStableIds() = false

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context?.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.img_view, mWidgetItem[position])

        val extra = Bundle()
        extra.putString(FavoriteWidget.EXTRA_ITEM, faList[position].title)

        val fillIntent = Intent()
        fillIntent.putExtras(extra)

        rv.setOnClickFillInIntent(R.id.img_view, fillIntent)
        return rv
    }

    override fun getCount() = mWidgetItem.size

    override fun getViewTypeCount() = 1

    override fun onDestroy() {
    }
}