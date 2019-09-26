package com.dolan.dolantancepapp.movie

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

class MovieAdapter(private val listener: (Movie?) -> Unit) :
    RecyclerView.Adapter<MovieAdapter.MovieHolder>() {

    private val movieList = mutableListOf<Movie?>()

    fun setMovieList(e: List<Movie?>) {
        movieList.clear()
        if (e.isNotEmpty()) {
            movieList.addAll(e)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        return MovieHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.data_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = movieList.size

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.bindItem(movieList[position], listener)
    }

    class MovieHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txtTitle: TextView = view.findViewById(R.id.txt_title)
        private val txtDate: TextView = view.findViewById(R.id.txt_date)
        private val txtRate: TextView = view.findViewById(R.id.txt_rate)
        private val imgPoster: ImageView = view.findViewById(R.id.img_poster)

        fun bindItem(e: Movie?, listener: (Movie?) -> Unit) {
            txtTitle.text = e?.title
            txtDate.text = getConvertDate(e?.releaseDate)
            txtRate.text = e?.voteAverage.toString()
            if (e?.posterPath != null) {
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