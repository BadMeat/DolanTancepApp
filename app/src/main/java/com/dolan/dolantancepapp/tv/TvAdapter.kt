package com.dolan.dolantancepapp.tv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dolan.dolantancepapp.BuildConfig
import com.dolan.dolantancepapp.R
import com.squareup.picasso.Picasso

class TvAdapter(private val itemList: List<ResultsItem?>) :
    RecyclerView.Adapter<TvAdapter.TvHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvHolder {
        return TvHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.data_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: TvHolder, position: Int) {
        holder.bindItem(itemList[position]!!)
    }

    class TvHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imgPoster: ImageView = view.findViewById(R.id.img_poster)
        private val txtTitle: TextView = view.findViewById(R.id.txt_title)
        private val txtDate: TextView = view.findViewById(R.id.txt_date)
        private val txtRate: TextView = view.findViewById(R.id.txt_rate)

        fun bindItem(e: ResultsItem) {
            txtTitle.text = e.name
            txtDate.text = e.firstAirDate
            txtRate.text = e.voteAverage.toString()
            Picasso.get().load("${BuildConfig.BASE_IMAGE}${e.posterPath}").into(imgPoster)
        }
    }
}