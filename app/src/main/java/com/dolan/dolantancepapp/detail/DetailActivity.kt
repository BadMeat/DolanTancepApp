package com.dolan.dolantancepapp.detail

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dolan.dolantancepapp.BuildConfig
import com.dolan.dolantancepapp.R
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.CONTENT_URI
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.DATE
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.POSTER
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.RATE
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.TITLE
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.TYPE
import com.dolan.dolantancepapp.favorite.FavoriteWidget
import com.dolan.dolantancepapp.invisible
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel

    private val detailTvList = mutableListOf<DetailTv>()
    private val detailMovieList = mutableListOf<DetailMovie>()
    private var detailCode = ""

    companion object {
        const val EXTRA_DETAIL_ID = "EXTRA_DETAIL_ID"
        const val EXTRA_TYPE = "EXTRA_TYPE"

        const val EXTRA_TV = "EXTRA_TV"
        const val EXTRA_MOVIE = "EXTRA_MOVIE"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favorite, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)
        detailCode = intent.getStringExtra(EXTRA_TYPE)
        val id = intent.getIntExtra(EXTRA_DETAIL_ID, 0)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)

        if (detailCode.equals(EXTRA_TV, true)) {
            Log.e("MOVIE","MASUK TV DONG")
            detailViewModel.getTvDetailList().observe(this, detailTvModel)
            detailViewModel.getTvData(id, "en-US")
        } else if (detailCode.equals(EXTRA_MOVIE, true)) {
            Log.e("MOVIE","MASUK MOVIE DONG")
            detailViewModel.getMovieDetailList().observe(this, detailMovieModel)
            detailViewModel.getMovieData(id, "en-US")
        }
    }

    private val detailTvModel = Observer<DetailTv> {
        if (it != null) {
            setUI(it, 1)
            detailTvList.add(it)
        }
        progress_bar.invisible()
    }

    private val detailMovieModel = Observer<DetailMovie> {
        if (it != null) {
            setUI(it, 2)
            detailMovieList.add(it)
        }
        progress_bar.invisible()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.menu_add -> {
                if (detailCode.equals(EXTRA_TV, true)) {
                    if (detailTvList.isNotEmpty()) {
                        addFavorite(detailTvList, 1)
                    } else {
                        Toast.makeText(
                            baseContext,
                            resources.getString(R.string.data_kosong),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else if (detailCode.equals(EXTRA_MOVIE, true)) {
                    if (detailMovieList.isNotEmpty()) {
                        addFavorite(detailMovieList, 2)
                    } else {
                        Toast.makeText(
                            baseContext,
                            resources.getString(R.string.data_kosong),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun <T> setUI(model: T, kode: Int) {
        when (kode) {
            1 -> {
                val detail = model as DetailTv
                txt_title.text = detail.name
                txt_date.text = detail.firstAirDate
                txt_rate.text = detail.voteAverage.toString()
                txt_descs.text = detail.overview
                Picasso.get().load("${BuildConfig.BASE_IMAGE}${detail.posterPath}").into(img_poster)
            }
            2 -> {
                val detail = model as DetailMovie
                txt_title.text = detail.title
                txt_date.text = detail.releaseDate
                txt_rate.text = detail.voteAverage.toString()
                txt_descs.text = detail.overview
                Picasso.get().load("${BuildConfig.BASE_IMAGE}${detail.posterPath}").into(img_poster)
            }
        }

    }

    private fun <T> addFavorite(list: T, kode: Int) {
        val values = ContentValues()
        when (kode) {
            1 -> {
                @Suppress("UNCHECKED_CAST")
                val tvList = list as MutableList<DetailTv>
                values.put(TITLE, tvList[0].name)
                values.put(DATE, tvList[0].firstAirDate)
                values.put(RATE, tvList[0].voteAverage)
                values.put(POSTER, tvList[0].posterPath)
                values.put(TYPE, 1)
            }
            2 -> {
                @Suppress("UNCHECKED_CAST")
                val movieList = list as MutableList<DetailMovie>
                values.put(TITLE, movieList[0].title)
                values.put(DATE, movieList[0].releaseDate)
                values.put(RATE, movieList[0].voteAverage)
                values.put(POSTER, movieList[0].posterPath)
                values.put(TYPE, 2)
            }
        }
        contentResolver?.insert(CONTENT_URI, values)

        val appWidget = AppWidgetManager.getInstance(this)
        val ids = appWidget.getAppWidgetIds(
            ComponentName(
                baseContext!!,
                FavoriteWidget::class.java
            )
        )
        appWidget.notifyAppWidgetViewDataChanged(
            ids,
            R.id.stack_view
        )

        Toast.makeText(
            baseContext,
            resources.getString(R.string.save_succes),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::detailViewModel.isInitialized) {
            detailViewModel.close()
        }
    }
}
