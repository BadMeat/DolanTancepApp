package com.dolan.dolantancepapp.favorite

import android.content.Intent
import android.widget.RemoteViewsService

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?) = StackRemoteViewFactory(this.applicationContext)
}