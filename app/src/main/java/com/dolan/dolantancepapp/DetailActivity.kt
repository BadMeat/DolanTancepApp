package com.dolan.dolantancepapp

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dolan.dolantancepapp.detail.DetailResponse
import com.dolan.dolantancepapp.detail.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: DetailViewModel

    private val detailList = mutableListOf<DetailResponse>()

    companion object {
        const val EXTRA_DETAIL = "EXTRA_DETAIL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val id = intent.getIntExtra(EXTRA_DETAIL, 0)
        Log.d("IDKU", "$id")
        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        detailViewModel.getDetailList().observe(this, detailModel)
        detailViewModel.getData(id, "en-US")
    }

    private val detailModel = Observer<DetailResponse> {
        if (it != null) {
            Log.d("DATANYA", "$it")
            setUI(it)
            detailList.add(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
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
