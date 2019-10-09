package com.dolan.dolantancepapp.tv


import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.dolan.dolantancepapp.detail.DetailActivity
import com.dolan.dolantancepapp.detail.DetailActivity.Companion.EXTRA_TV
import com.dolan.dolantancepapp.detail.DetailActivity.Companion.EXTRA_TYPE
import com.dolan.dolantancepapp.getLanguage
import com.dolan.dolantancepapp.invisible
import com.dolan.dolantancepapp.visible
import kotlinx.android.synthetic.main.fragment_tv.*
import java.util.*

class TvFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var tvViewModel: TvViewModel
    private lateinit var tvAdapter: TvAdapter
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_tv, container, false)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val myMenu = menu.findItem(R.id.menu_search)
        searchView = myMenu?.actionView as SearchView

        searchView.setOnQueryTextListener(this)

        clearSearch()
        hideVirtualKeyboard()
        searchView.setOnCloseListener {
            progress_bar.visible()
            tvViewModel.getTvPopular(getLanguage())
            false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvViewModel = ViewModelProviders.of(this).get(TvViewModel::class.java)
        tvViewModel.getTvList().observe(this, getTv)
        tvViewModel.getTvPopular(getLanguage())

        tvAdapter = TvAdapter {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DETAIL_ID, it.id)
            intent.putExtra(EXTRA_TYPE, EXTRA_TV)
            startActivity(intent)
        }

        recycler_view.adapter = tvAdapter
        recycler_view.layoutManager = GridLayoutManager(context, 2)
    }

    private val getTv = Observer<MutableList<ResultsItem?>> { t ->
        if (t.isNotEmpty()) {
            tvAdapter.setTvList(t)
        } else {
            tvAdapter.setTvList(mutableListOf())
            Toast.makeText(context, resources.getString(R.string.data_kosong), Toast.LENGTH_SHORT)
                .show()
        }
        progress_bar.invisible()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        tvViewModel.getTv(getLanguage(), query)
        progress_bar.visible()
        hideVirtualKeyboard()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    /**
     * Hide virtual keyboard
     */
    private fun hideVirtualKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    /**
     * Clear search view
     */
    private fun clearSearch() {
        searchView.onActionViewCollapsed()
        searchView.setQuery("", false)
        searchView.clearFocus()
    }
}
