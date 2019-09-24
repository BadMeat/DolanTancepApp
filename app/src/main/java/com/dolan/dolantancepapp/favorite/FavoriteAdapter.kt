package com.dolan.dolantancepapp.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dolan.dolantancepapp.BuildConfig
import com.dolan.dolantancepapp.R
import com.dolan.dolantancepapp.db.Favorite
import com.squareup.picasso.Picasso

class FavoriteAdapter(private val listerner: (Favorite) -> Unit) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>() {

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
        holder.bindItem(favList[position], listerner)
    }

    class FavoriteHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val txtTitle: TextView = view.findViewById(R.id.txt_title)
        private val txtDate: TextView = view.findViewById(R.id.txt_date)
        private val txtRate: TextView = view.findViewById(R.id.txt_rate)
        private val imgPoster: ImageView = view.findViewById(R.id.img_poster)

        fun bindItem(e: Favorite, listener: (Favorite) -> Unit) {
            txtTitle.text = e.title
            txtDate.text = e.date
            txtRate.text = e.rate.toString()
            if (e.poster != null) {
                Picasso.get().load("${BuildConfig.BASE_IMAGE}${e.poster}").into(imgPoster)
            } else {
                Picasso.get().load(R.drawable.noimage_rv).into(imgPoster)
            }
            itemView.setOnClickListener {
                listener(e)
            }
        }
    }
}