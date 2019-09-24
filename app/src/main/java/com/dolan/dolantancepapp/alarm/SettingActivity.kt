package com.dolan.dolantancepapp.alarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dolan.dolantancepapp.R
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val reminderService = TvReminderService()

        sw_daily.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                reminderService.setDaily(
                    baseContext,
                    "Dolan Tancep",
                    "Haloo yuk cari cari film"
                )
            }
        }
        sw_release.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                reminderService.setRelease(baseContext)
            }
        }
    }
}
