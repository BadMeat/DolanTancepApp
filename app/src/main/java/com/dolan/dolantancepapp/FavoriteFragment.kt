package com.dolan.dolantancepapp


import android.content.Context
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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.dolan.dolantancepapp.database.FavoriteViewModel
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.CONTENT_URI
import com.dolan.dolantancepapp.db.Favorite
import com.dolan.dolantancepapp.db.FavoriteAdapter
import com.dolan.dolantancepapp.db.LoadFavCallback
import com.dolan.dolantancepapp.db.MappingHelper.Companion.mapCursorToArrayList
import kotlinx.android.synthetic.main.fragment_favorite.*
import java.lang.ref.WeakReference

class FavoriteFragment : Fragment(), LoadFavCallback {

    companion object {
        const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun preExecute() {

    }

    override fun postExecute(cursor: Cursor?) {
        if (cursor != null) {
            val listFav = mapCursorToArrayList(cursor)
            if (listFav.isNotEmpty()) {
                favAdapter.setFavList(listFav)
            } else {
                favAdapter.setFavList(mutableListOf())
                Toast.makeText(context, "Data Kosong", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Cursor Kosong", Toast.LENGTH_SHORT).show()
        }

    }

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var favAdapter: FavoriteAdapter

    private val favList: MutableList<Favorite> = mutableListOf()
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

        favAdapter = FavoriteAdapter()
        handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        myObserver = DataObserver(handler, context)


        context?.contentResolver?.registerContentObserver(CONTENT_URI, true, myObserver)

        recycler_view.adapter = favAdapter
        recycler_view.layoutManager = GridLayoutManager(context, 2)


        if (savedInstanceState == null) {
            if (context != null) {
                LoadFavAsyn(context!!, this).execute()
            }
        } else {
            val list: List<Favorite>? = savedInstanceState.getParcelableArrayList(EXTRA_STATE)
            if (list != null) {
                favAdapter.setFavList(list)
            }
        }

//        viewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
//        viewModel.getFavoriteData().observe(this, getFavorite)
//        viewModel.getFavoriteData(context!!)
    }

    private val getFavorite = Observer<MutableList<Favorite>> {
        if (it != null) {
            favList.clear()
            favList.addAll(it)
            favAdapter.notifyDataSetChanged()
        }
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
//            if (context != null) {
//                val fragment: LoadFavCallback? = context
//                if (fragment != null) {
//                    LoadFavAsyn(context, fragment).execute()
//                }
//            }
        }
    }
}
