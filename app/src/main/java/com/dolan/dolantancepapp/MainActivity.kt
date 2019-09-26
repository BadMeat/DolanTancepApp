package com.dolan.dolantancepapp

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dolan.dolantancepapp.alarm.SettingActivity
import com.dolan.dolantancepapp.db.LoadFavCallback
import com.dolan.dolantancepapp.favorite.FavoriteFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), LoadFavCallback {

    override fun preExecute() {

    }

    override fun postExecute(cursor: Cursor?) {
        val fragment =
            supportFragmentManager.primaryNavigationFragment as NavHostFragment
        val favFragment =
            fragment.childFragmentManager.primaryNavigationFragment

        if (favFragment is FavoriteFragment) {
            favFragment.postExecute(cursor)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = Navigation.findNavController(this, R.id.fragment_main)
        nav_main.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_setting -> {
                startActivity(Intent(this, SettingActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
