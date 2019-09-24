package com.dolan.dolantancepapp.movie


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
import com.dolan.dolantancepapp.detail.DetailActivity.Companion.EXTRA_MOVIE
import com.dolan.dolantancepapp.detail.DetailActivity.Companion.EXTRA_TYPE
import com.dolan.dolantancepapp.invisible
import com.dolan.dolantancepapp.visible
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var searchView: SearchView
    private val movieObserver = Observer<MutableList<Movie?>> {
        if (it.isNotEmpty()) {
            movieAdapter.setMovieList(it)
        } else {
            movieAdapter.setMovieList(mutableListOf())
            Toast.makeText(context, R.string.data_kosong, Toast.LENGTH_SHORT).show()
        }
        progress_bar.invisible()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        movieViewModel.getData("en-US", query)
        progress_bar.visible()
        hideVirtualKeyboard()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val myMenu = menu.findItem(R.id.menu_search)
        searchView = myMenu?.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        clearSearch()
        hideVirtualKeyboard()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = MovieAdapter {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DETAIL_ID, it?.id)
            intent.putExtra(EXTRA_TYPE, EXTRA_MOVIE)
            startActivity(intent)
        }
        rv_movie.adapter = movieAdapter
        rv_movie.layoutManager = GridLayoutManager(context, 2)

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        movieViewModel.getMovieList().observe(this, movieObserver)
    }


    override fun onDestroy() {
        super.onDestroy()
        if (::movieViewModel.isInitialized) {
            movieViewModel.closeDispose()
        }
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
