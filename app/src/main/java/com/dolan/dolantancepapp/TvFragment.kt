package com.dolan.dolantancepapp


import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dolan.dolantancepapp.favorite.FavoriteWidget
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
        /**
         * Clear search view
         */
        searchView.onActionViewCollapsed()
        searchView.setQuery("", false)
        searchView.clearFocus()
        /**
         * Hide virtual keyboard
         */
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)

        searchView.setOnQueryTextListener(this)
        searchView.setOnCloseListener {
            setTv("en-US", "")
            false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(TvViewModel::class.java)
        viewModel.getTvList().observe(this, getTv)

        tvAdapter = TvAdapter(tvList) {
            val intent = Intent(context,DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DETAIL, it.id)
            startActivity(intent)
        }

        recycler_view.adapter = tvAdapter
        recycler_view.layoutManager = GridLayoutManager(context, 2)
    }

    private val getTv = Observer<MutableList<ResultsItem?>> { t ->
        if (t != null) {
            tvList.clear()
            tvList.addAll(t)
            tvAdapter.notifyDataSetChanged()
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        /**
         * Clear search view
         */
        searchView.onActionViewCollapsed()
        searchView.setQuery("", false)
        searchView.clearFocus()
        if (query != null) {
            setTv("en-US", query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
//        progress_bar.visible()
//        if (newText != null) {
//            setTv("en-US", newText)
//        }
        return false
    }

    private fun setTv(language: String, title: String) {
        viewModel.getTv(language, title)
        tvAdapter.notifyDataSetChanged()
        progress_bar.invisible()
    }

    private fun updateWidget() {
        Log.d("TvFragment", "Update From Fragment")
//        val intent = Intent(context, FavoriteWidget::class.java)
//        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
//        val ids = AppWidgetManager.getInstance(context)
//            .getAppWidgetIds(ComponentName(context!!, FavoriteWidget::class.java))
//        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
//        context?.sendBroadcast(intent)

//        val appWidgetManager = AppWidgetManager.getInstance(context)
//        val views = RemoteViews(context?.packageName, R.layout.favorite_widget)
//        val thisWidget = ComponentName(context!!, FavoriteWidget::class.java)
//        views.setRemoteAdapter(R.id.stack_view, intent)
//        views.setEmptyView(R.id.stack_view, R.id.empty_view)
//        appWidgetManager.updateAppWidget(thisWidget, views)

        val appWidgetManager = AppWidgetManager.getInstance(context)

        val appWidgetId = AppWidgetManager.getInstance(context)
            .getAppWidgetIds(ComponentName(context!!, FavoriteWidget::class.java))

        val myWidget = FavoriteWidget()
        myWidget.onUpdate(context!!, appWidgetManager, appWidgetId)
    }
}
