package com.dolan.dolantancepapp


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dolan.dolantancepapp.database.Favorite
import com.dolan.dolantancepapp.database.FavoriteAdapter
import com.dolan.dolantancepapp.database.FavoriteViewModel
import com.dolan.dolantancepapp.database.database
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment() {

    private lateinit var viewModel: FavoriteViewModel

    private val favList: MutableList<Favorite> = mutableListOf()
    private lateinit var favAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favAdapter = FavoriteAdapter(favList) {
            viewModel.deleteFavoriteData(context?.database, it.id!!)
            Toast.makeText(
                context,
                resources.getString(R.string.delete_success),
                Toast.LENGTH_SHORT
            ).show()
            viewModel.getFavoriteData(context!!)
        }
        recycler_view.adapter = favAdapter
        recycler_view.layoutManager = GridLayoutManager(context, 2)

        viewModel = ViewModelProviders.of(this).get(FavoriteViewModel::class.java)
        viewModel.getFavoriteData().observe(this, getFavorite)
        viewModel.getFavoriteData(context!!)
    }

    private val getFavorite = Observer<MutableList<Favorite>> {
        if (it != null) {
            favList.clear()
            favList.addAll(it)
            favAdapter.notifyDataSetChanged()
        }
    }
}
