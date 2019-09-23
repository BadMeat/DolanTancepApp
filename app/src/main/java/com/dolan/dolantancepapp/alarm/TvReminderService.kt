package com.dolan.dolantancepapp.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.dolan.dolantancepapp.MainActivity
import com.dolan.dolantancepapp.R
import com.dolan.dolantancepapp.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class TvReminderService : BroadcastReceiver() {


    companion object {
        const val CHANNEL_NAME = "tv_notif"
        const val CHANNEL_ID = "tv_id"

        const val GROUP_TV = "group_tv"
        const val ID_NOTIF = 100

        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_MESSAGE = "extra_messages"

        const val REQUEST_PENDING_MAIN = 0

        const val MAX_REQUEST = 2
        const val ACTION_DAILY = "com.dolan.dolantancepapp.alarm.ACTION_DAILY"
        const val ACTION_RELEASE = "com.dolan.dolantancepapp.alarm.ACTION_RELEASE"

        const val COUNTER = "com.dolan.dolantancepapp.alarm.COUNTER"

    }

    @SuppressLint("CheckResult")
    private fun getData(ctx: Context?) {
        val today = Calendar.getInstance().time
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val tvList = mutableListOf<ResultsItem?>()

        ApiClient.instance.getTvRelase(format.format(today), format.format(today))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map {
                it.body()?.results
            }
            .doFinally {
                for (i: ResultsItem? in tvList) {
                    showPushReleaseNotification(ctx, tvList)
                }
                val sharedPreferences = ctx?.getSharedPreferences(COUNTER, MODE_PRIVATE)
                sharedPreferences?.edit()?.putInt(COUNTER, 0)?.apply()
            }
            .subscribe(
                { result ->
                    Log.d("RESULTNYACINGCAAAW", "$result")
                    if (result != null) {
                        tvList.addAll(result)
                    }
                },
                { error -> Log.d("Error Request Alarm", "$error") }
            )
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("ONRECEIVERRERR", "MASUK SINI ODNG")
        val title = intent?.getStringExtra(EXTRA_TITLE)
        val message = intent?.getStringExtra(EXTRA_MESSAGE)
        Log.d("ACTIONRECEIVER", intent?.action)
        when (intent?.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                showPushNotification(context, title, message)
            }
            ACTION_RELEASE -> {
                getData(context)
            }
            else -> {
//                showPushNotification(context, title, message)
                getData(context)
            }
        }
    }

    fun setDailyRepeat(ctx: Context?, title: String?, message: String?, action: String) {
        val alarmManager = ctx?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(ctx, TvReminderService::class.java)
        intent.putExtra(EXTRA_TITLE, title)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.action = action
        val pendingIntent =
            PendingIntent.getBroadcast(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = 27
        calendar.set(year, month, day, hour, minute, 0)

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        val sharedPreferences = ctx.getSharedPreferences(COUNTER, MODE_PRIVATE)
        sharedPreferences?.edit()?.putInt(COUNTER, 0)?.apply()

        Toast.makeText(ctx, ctx.resources.getString(R.string.alarm_set), Toast.LENGTH_SHORT).show()
    }

    fun setRelease(ctx: Context?) {
        val alarmManager = ctx?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(ctx, TvReminderService::class.java)
        intent.action = ACTION_RELEASE
        val pendingIntent =
            PendingIntent.getActivity(ctx, 9, intent, 0)
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = 7
        calendar.set(year, month, day, hour, minute, 0)

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Log.d("ALARMSETON", "${calendar.time}")
        Toast.makeText(ctx, "Alarm Release Set On ${calendar.timeInMillis}", Toast.LENGTH_SHORT)
            .show()
    }

    private fun showPushReleaseNotification(ctx: Context?, tvRelease: List<ResultsItem?>) {
        val notificationManager =
            ctx?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(ctx, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            ctx,
            REQUEST_PENDING_MAIN,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val sharedPreferences = ctx.getSharedPreferences(COUNTER, MODE_PRIVATE)
        var counter = sharedPreferences.getInt(COUNTER, 0)
        Log.d("COUNTERKUU========", "$counter")

        val notificationBuilder = NotificationCompat.Builder(ctx, CHANNEL_NAME)
            .setContentTitle(tvRelease[counter]?.name)
            .setContentText(tvRelease[counter]?.overview)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notif)
            .setGroup(GROUP_TV)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationBuilder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        if (counter > MAX_REQUEST) {
            val style = NotificationCompat.InboxStyle()
            style.addLine("First TV ${tvRelease[counter]?.name}")
            style.addLine("First TV ${tvRelease[counter - 1]?.name}")
            style.setBigContentTitle("INI BIG CONTENT TITLE")
            style.setSummaryText("INI SUMMARY LHO")
            notificationBuilder.setStyle(style)
            notificationBuilder.setGroupSummary(true)
        }

        counter++
        sharedPreferences.edit().putInt(COUNTER, counter).apply()

        notificationManager.notify(counter, notificationBuilder.build())
    }


    private fun showPushNotification(ctx: Context?, title: String?, message: String?) {
        val notificationManager =
            ctx?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(ctx, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            ctx,
            REQUEST_PENDING_MAIN,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(ctx, CHANNEL_NAME)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notif)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationBuilder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(ID_NOTIF, notificationBuilder.build())
    }
}