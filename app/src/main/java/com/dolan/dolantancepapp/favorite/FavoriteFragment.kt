package com.dolan.dolantancepapp.favorite


import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.database.Cursor
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.dolan.dolantancepapp.R
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.CONTENT_URI
import com.dolan.dolantancepapp.db.LoadFavCallback
import com.dolan.dolantancepapp.db.MappingHelper.Companion.mapCursorToArrayList
import com.dolan.dolantancepapp.detail.DetailActivity
import com.dolan.dolantancepapp.invisible
import com.dolan.dolantancepapp.visible
import kotlinx.android.synthetic.main.fragment_favorite.*
import java.lang.ref.WeakReference

class FavoriteFragment : Fragment(), LoadFavCallback {

    private lateinit var favAdapter: FavoriteAdapter
    private lateinit var handlerThread: HandlerThread
    private lateinit var myObserver: ContentObserver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favAdapter = FavoriteAdapter {
            val intent = Intent(context, DetailActivity::class.java)
            val type = when (it.type) {
                1 -> DetailActivity.EXTRA_TV
                else -> DetailActivity.EXTRA_MOVIE
            }
            intent.putExtra(DetailActivity.EXTRA_TYPE, type)
            intent.putExtra(DetailActivity.EXTRA_DETAIL_ID, it.id)
            startActivity(intent)
        }
        handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        myObserver =
            DataObserver(handler, context)

        context?.contentResolver?.registerContentObserver(CONTENT_URI, true, myObserver)

        recycler_view.adapter = favAdapter
        recycler_view.layoutManager = GridLayoutManager(context, 2)

        LoadFavAsyn(context!!, this).execute()
    }

    class LoadFavAsyn(
        ctx: Context,
        loadCallBack: LoadFavCallback
    ) : AsyncTask<Void, Void, Cursor?>() {

        private val weakContext = WeakReference(ctx)
        private val weakLoadCallBack = WeakReference(loadCallBack)

        override fun onPreExecute() {
            super.onPreExecute()
            weakLoadCallBack.get()?.preExecute()
        }

        override fun doInBackground(vararg params: Void?): Cursor? {
            val contexte = weakContext.get()
            return contexte?.contentResolver?.query(CONTENT_URI, null, null, null, null)
        }

        override fun onPostExecute(result: Cursor?) {
            super.onPostExecute(result)
            weakLoadCallBack.get()?.postExecute(result)
        }
    }

    class DataObserver(handler: Handler, ctx: Context?) : ContentObserver(handler) {
        val context = ctx

        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            if (context != null) {
                val fragment: LoadFavCallback? = context as LoadFavCallback
                if (fragment != null) {
                    LoadFavAsyn(
                        context,
                        fragment
                    ).execute()
                }
            }
        }
    }

    override fun preExecute() {
        progress_bar.visible()
    }

    override fun postExecute(cursor: Cursor?) {
        if (cursor != null) {
            val listFav = mapCursorToArrayList(cursor)
            if (listFav.isNotEmpty()) {
                favAdapter.setFavList(listFav)
            } else {
                favAdapter.setFavList(mutableListOf())
                Toast.makeText(
                    context,
                    resources.getString(R.string.data_kosong),
                    Toast.LENGTH_SHORT
                ).show()
            }
            progress_bar.invisible()
        }
    }
}
