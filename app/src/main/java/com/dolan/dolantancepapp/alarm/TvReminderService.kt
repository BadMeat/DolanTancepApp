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
import com.dolan.dolantancepapp.movie.Movie
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
        const val ID_NOTIFICATION = 100
        const val ID_SUMMARY = 300

        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_MESSAGE = "extra_messages"

        const val REQUEST_PENDING_MAIN = 0

        const val REQUEST_ALARM_DAILY = 101
        const val REQUEST_ALARM_RELEASE = 201

        const val ACTION_DAILY = "com.dolan.dolantancepapp.alarm.ACTION_DAILY"
        const val ACTION_RELEASE = "com.dolan.dolantancepapp.alarm.ACTION_RELEASE"

        const val COUNTER = "com.dolan.dolantancepapp.alarm.COUNTER"

    }

    @SuppressLint("CheckResult")
    private fun getData(ctx: Context?) {
        val today = Calendar.getInstance().time
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val tvList = mutableListOf<Movie?>()

        ApiClient.instance.getMovieRelease(format.format(today), format.format(today))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map {
                it.body()?.results
            }
            .doFinally {
                showRelease(ctx, tvList)
                val sharedPreferences = ctx?.getSharedPreferences(COUNTER, MODE_PRIVATE)
                sharedPreferences?.edit()?.putInt(COUNTER, 0)?.apply()
            }
            .subscribe(
                { result ->
                    if (result != null) {
                        tvList.addAll(result)
                    }
                },
                { error -> Log.d("Error Request Alarm", "$error") }
            )
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra(EXTRA_TITLE)
        val message = intent?.getStringExtra(EXTRA_MESSAGE)

        when (intent?.action) {
            Intent.ACTION_BOOT_COMPLETED -> {

                val sharedDaily =
                    context?.getSharedPreferences(SettingActivity.EXTRA_DAILY, MODE_PRIVATE)
                val sharedRelease =
                    context?.getSharedPreferences(SettingActivity.EXTRA_RELEASE, MODE_PRIVATE)

                val daily = sharedDaily?.getBoolean(SettingActivity.EXTRA_DAILY, false)
                val release = sharedRelease?.getBoolean(SettingActivity.EXTRA_RELEASE, false)

                if (daily != null) {
                    if (daily) {
                        showDaily(context, title, message)
                    }
                }
                if (release != null) {
                    if (release) {
                        getData(context)
                    }
                }
            }
            ACTION_DAILY -> {
                showDaily(context, title, message)
            }
            ACTION_RELEASE -> {
                getData(context)
            }
            else -> {
                showDaily(context, title, message)
            }
        }
    }

    fun setDaily(ctx: Context?, title: String?, message: String?) {
        val alarmManager = ctx?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(ctx, TvReminderService::class.java)
        intent.putExtra(EXTRA_TITLE, title)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.action = ACTION_DAILY
        val pendingIntent =
            PendingIntent.getBroadcast(
                ctx,
                REQUEST_ALARM_DAILY,
                intent,
                0
            )
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

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
            PendingIntent.getBroadcast(
                ctx,
                REQUEST_ALARM_RELEASE,
                intent,
                0
            )
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 23)
        calendar.set(Calendar.SECOND, 0)

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

    private fun showRelease(ctx: Context?, tvRelease: List<Movie?>) {
        val notificationManager =
            ctx?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(ctx, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            ctx,
            REQUEST_PENDING_MAIN,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        var counter = 0

        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(ctx, CHANNEL_NAME)

        for (i in tvRelease) {
            notificationBuilder
                .setContentTitle(i?.title)
                .setContentText(i?.overview)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notif)
                .setGroup(GROUP_TV)
                .setAutoCancel(true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationBuilder.setChannelId(CHANNEL_ID)
                notificationManager.createNotificationChannel(notificationChannel)
            }

            notificationManager.notify(counter, notificationBuilder.build())
            counter++
        }

        if (counter > 1) {
            val style = NotificationCompat.InboxStyle()
            style.addLine("New Release ${tvRelease[1]?.title}")
            style.addLine("New Release ${tvRelease[0]?.title}")
            style.setBigContentTitle("$counter New Release Film")
            style.setSummaryText("New Movie Release")
            notificationBuilder
                .setContentIntent(pendingIntent)
                .setContentTitle("$counter New Release Film")
                .setContentText("Release")
                .setSmallIcon(R.drawable.ic_notif)
                .setGroup(GROUP_TV)
                .setGroupSummary(true)
                .setStyle(style)
                .setAutoCancel(true)
            notificationManager.notify(ID_SUMMARY, notificationBuilder.build())
        }
    }


    private fun showDaily(ctx: Context?, title: String?, message: String?) {
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

        notificationManager.notify(ID_NOTIFICATION, notificationBuilder.build())
    }

    fun stopJob(ctx: Context, request: Int) {
        val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(ctx, TvReminderService::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(ctx, request, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
        Toast.makeText(ctx, "Alarm Release", Toast.LENGTH_SHORT).show()
    }
}