package com.dolan.dolantancepapp.db

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dolan.dolantancepapp.BuildConfig
import com.dolan.dolantancepapp.R
import com.squareup.picasso.Picasso

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>() {

    private val favList = mutableListOf<Favorite>()

    fun setFavList(e: List<Favorite>) {
        favList.clear()
        favList.addAll(e)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        return FavoriteHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.data_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = favList.size

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        holder.bindItem(favList[position], position)
    }

    class FavoriteHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val txtTitle: TextView = view.findViewById(R.id.txt_title)
        private val txtDate: TextView = view.findViewById(R.id.txt_date)
        private val txtRate: TextView = view.findViewById(R.id.txt_rate)
        private val imgPoster: ImageView = view.findViewById(R.id.img_poster)

        fun bindItem(e: Favorite, position: Int) {
            txtTitle.text = e.title
            txtDate.text = e.date
            txtRate.text = e.rate.toString()
            Picasso.get().load("${BuildConfig.BASE_IMAGE}${e.poster}").into(imgPoster)
            itemView.setOnClickListener(
                CustomItemClicked(
                    position,
                    object : CustomItemClicked.OnItemClicked {
                        override fun itemClicked(view: View?, position: Int) {

                        }
                    })
            )
        }
    }
}