package com.dolan.dolantancepapp.alarm

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.dolan.dolantancepapp.R
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DAILY = "com.dolan.dolantancepapp.alarm.EXTRA_DAILY"
        const val EXTRA_RELEASE = "com.dolan.dolantancepapp.alarm.EXTRA_RELEASE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val reminderService = TvReminderService()

        val sharedDaily = baseContext.getSharedPreferences(EXTRA_DAILY, Context.MODE_PRIVATE)
        val sharedRelease = baseContext.getSharedPreferences(EXTRA_RELEASE, Context.MODE_PRIVATE)

        var daily = sharedDaily.getBoolean(EXTRA_DAILY, false)
        var release = sharedRelease.getBoolean(EXTRA_RELEASE, false)

        sw_daily.isChecked = daily
        sw_release.isChecked = release

        sw_daily.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                reminderService.setDaily(
                    baseContext,
                    "Dolan Tancep",
                    "Haloo yuk cari cari film"
                )
            } else {
                reminderService.stopJob(baseContext, TvReminderService.REQUEST_ALARM_DAILY)
            }
            daily = !daily
            sharedDaily.edit().putBoolean(EXTRA_DAILY, daily).apply()
        }

        sw_release.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                reminderService.setRelease(baseContext)
            } else {
                reminderService.stopJob(baseContext, TvReminderService.REQUEST_ALARM_RELEASE)
            }
            release = !release
            sharedRelease.edit().putBoolean(EXTRA_RELEASE, release).apply()
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
}
