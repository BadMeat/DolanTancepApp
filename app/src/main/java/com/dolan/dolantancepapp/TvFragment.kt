package com.dolan.dolantancepapp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dolan.dolantancepapp.tv.ResultsItem
import com.dolan.dolantancepapp.tv.TvAdapter
import com.dolan.dolantancepapp.tv.TvViewModel
import kotlinx.android.synthetic.main.fragment_tv.*

class TvFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var viewModel: TvViewModel
    private val tvList: MutableList<ResultsItem?> = mutableListOf()
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
        searchView.setOnCloseListener {
            setTv("en-US", "")
            false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvAdapter = TvAdapter(tvList)

        recycler_view.adapter = tvAdapter
        recycler_view.layoutManager = GridLayoutManager(context, 2)

        viewModel = ViewModelProviders.of(this).get(TvViewModel::class.java)
        viewModel.getTvList().observe(this, getTv)


    }

    private val getTv = Observer<MutableList<ResultsItem?>> { t ->
        if (t != null) {
            tvList.clear()
            tvList.addAll(t)
            tvAdapter.notifyDataSetChanged()
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        progress_bar.visible()
        if (newText != null) {
            setTv("en-US", newText)
            progress_bar.invisible()
        }
        return true
    }

    private fun setTv(language: String, title: String) {
        viewModel.getTv(language, title)
        tvAdapter.notifyDataSetChanged()
    }
}
