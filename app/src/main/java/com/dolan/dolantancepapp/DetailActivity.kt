package com.dolan.dolantancepapp

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.CONTENT_URI
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.DATE
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.POSTER
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.RATE
import com.dolan.dolantancepapp.db.DatabaseContract.Companion.TITLE
import com.dolan.dolantancepapp.detail.DetailResponse
import com.dolan.dolantancepapp.detail.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel

    private val detailList = mutableListOf<DetailResponse>()

    companion object {
        const val EXTRA_DETAIL = "EXTRA_DETAIL"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favorite, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val id = intent.getIntExtra(EXTRA_DETAIL, 0)
        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        detailViewModel.getDetailList().observe(this, detailModel)
        detailViewModel.getData(id, "en-US")
    }

    private val detailModel = Observer<DetailResponse> {
        if (it != null) {
            setUI(it)
            detailList.add(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.menu_add -> {
                val values = ContentValues()
                values.put(TITLE, detailList[0].name)
                values.put(DATE, detailList[0].firstAirDate)
                values.put(RATE, detailList[0].voteAverage)
                values.put(POSTER, detailList[0].posterPath)
                Log.d("Values", "$values")
                contentResolver?.insert(CONTENT_URI, values)
                Toast.makeText(
                    baseContext,
                    resources.getString(R.string.save_succes),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUI(detail: DetailResponse) {
        txt_title.text = detail.name
        txt_date.text = detail.firstAirDate
        txt_rate.text = detail.voteAverage.toString()
        txt_descs.text = detail.overview
    }
}
