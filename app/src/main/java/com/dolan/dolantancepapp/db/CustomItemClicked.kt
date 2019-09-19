package com.dolan.dolantancepapp.db

import android.view.View

class CustomItemClicked(
    private val position: Int,
    private val onItemCallBack: OnItemClicked
) : View.OnClickListener {

    override fun onClick(v: View?) {
        onItemCallBack.itemClicked(v, position)
    }

    interface OnItemClicked {
        fun itemClicked(view: View?, position: Int)
    }
}