package com.dolan.dolantancepapp.tv

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dolan.dolantancepapp.BuildConfig
import com.dolan.dolantancepapp.R
import com.dolan.dolantancepapp.getConvertDate
import com.squareup.picasso.Picasso

class TvAdapter(
    private val listener: (ResultsItem) -> Unit
) :
    RecyclerView.Adapter<TvAdapter.TvHolder>() {

    private val tvList = mutableListOf<ResultsItem?>()

    fun setTvList(e: List<ResultsItem?>) {
        tvList.clear()
        if (e.isNotEmpty()) {
            tvList.addAll(e)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvHolder {
        return TvHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.data_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = tvList.size

    override fun onBindViewHolder(holder: TvHolder, position: Int) {
        holder.bindItem(tvList[position]!!, listener)
    }

    class TvHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imgPoster: ImageView = view.findViewById(R.id.img_poster)
        private val txtTitle: TextView = view.findViewById(R.id.txt_title)
        private val txtDate: TextView = view.findViewById(R.id.txt_date)
        private val txtRate: TextView = view.findViewById(R.id.txt_rate)

        fun bindItem(e: ResultsItem, listener: (ResultsItem) -> Unit) {
            txtTitle.text = e.name
            txtDate.text = getConvertDate(e.firstAirDate)
            txtRate.text = e.voteAverage.toString()
            if (e.posterPath != null) {
                Picasso.get().load("${BuildConfig.BASE_IMAGE}${e.posterPath}").into(imgPoster)
            } else {
                Picasso.get().load(R.drawable.noimage_rv).into(imgPoster)
            }
            itemView.setOnClickListener {
                listener(e)
            }
        }
    }
}