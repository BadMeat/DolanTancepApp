package com.dolan.dolantancepapp.favorite


import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dolan.dolantancepapp.R
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.CONTENT_URI
import com.dolan.dolantancepapp.db.Favorite
import com.dolan.dolantancepapp.detail.DetailActivity
import com.dolan.dolantancepapp.invisible
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment() {

    private lateinit var favAdapter: FavoriteAdapter
    private lateinit var handlerThread: HandlerThread
    private lateinit var myObserver: ContentObserver

    private lateinit var favViewModel: FavoriteViewModel

    private val favList = Observer<List<Favorite>> {
        if (it != null) {
            favAdapter.setFavList(it)
            progress_bar.invisible()
        } else {
            favAdapter.setFavList(mutableListOf())
            Toast.makeText(
                context,
                resources.getString(R.string.data_kosong),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val myMenu = menu.findItem(R.id.menu_search)
        val searchView = myMenu?.actionView as SearchView

        searchView.onActionViewCollapsed()
        searchView.setQuery("", false)
        searchView.clearFocus()
        hideVirtualKeyboard()
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
            FavoriteViewModel.DataObserver(handler, context)

        context?.contentResolver?.registerContentObserver(CONTENT_URI, true, myObserver)

        recycler_view.adapter = favAdapter
        recycler_view.layoutManager = GridLayoutManager(context, 2)

        favViewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        favViewModel.getFavList().observe(this, favList)
        favViewModel.getData(context!!)
    }

    fun postExecute() {
        favViewModel.getData(context!!)
    }

    /**
     * Hide virtual keyboard
     */
    private fun hideVirtualKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}
