package com.dolan.dolantancepapp.favorite

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.dolan.dolantancepapp.BuildConfig
import com.dolan.dolantancepapp.R
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.CONTENT_URI
import com.dolan.dolantancepapp.db.Favorite
import com.dolan.dolantancepapp.db.MappingHelper
import com.squareup.picasso.Picasso

class StackRemoteViewFactory(ctx: Context) : RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItem = ArrayList<Bitmap>()
    private val favList = mutableListOf<Favorite>()
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

        val cursor = context?.contentResolver?.query(CONTENT_URI, null, null, null, null)
        val data = MappingHelper.mapCursorToArrayList(cursor)
        favList.clear()
        mWidgetItem.clear()
        favList.addAll(data.toMutableList())

        for (i: Favorite in favList) {
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
        if(mWidgetItem.isNotEmpty()){
            rv.setImageViewBitmap(R.id.img_view, mWidgetItem[position])

            val extra = Bundle()
            extra.putString(FavoriteWidget.EXTRA_ITEM, favList[position].title)

            val fillIntent = Intent()
            fillIntent.putExtras(extra)

            rv.setOnClickFillInIntent(R.id.img_view, fillIntent)
        }
        return rv
    }

    override fun getCount() = mWidgetItem.size

    override fun getViewTypeCount() = 1

    override fun onDestroy() {
    }
}