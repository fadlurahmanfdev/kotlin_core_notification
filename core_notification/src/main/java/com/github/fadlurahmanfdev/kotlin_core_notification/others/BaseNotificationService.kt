package com.github.fadlurahmanfdev.kotlin_core_notification.others

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat

abstract class BaseNotificationService {
    private lateinit var notificationManager: NotificationManager

    fun getNotificationManager(context: Context): NotificationManager {
        if (!this::notificationManager.isInitialized) {
            notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return notificationManager
    }

     fun baseCreateNotificationChannel(
        context: Context,
        channelId: String,
        channelName: String,
        channelDescription: String,
        sound:Uri
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!baseIsNotificationChannelExist(context, channelId = channelId)) {
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = channelDescription
                    setSound(sound, null)
                }
                getNotificationManager(context).createNotificationChannel(channel)
            }
        }
    }

     fun baseIsNotificationChannelExist(context: Context, channelId: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val allChannels = getNotificationManager(context).notificationChannels
            var knownChannel: NotificationChannel? = null
            for (element in allChannels) {
                if (element.id == channelId) {
                    knownChannel = element
                    break
                }
            }
            return knownChannel != null
        }
        return false
    }

    fun notificationBuilder(
        context: Context,
        channelId: String,
        @DrawableRes smallIcon: Int,
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channelId)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSmallIcon(smallIcon)
    }

    fun groupNotificationBuilder(
        context: Context,
        channelId: String,
        groupKey: String,
        bigContentTitle: String,
        summaryText: String,
        lines: List<String>,
        @DrawableRes smallIcon: Int,
    ): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context, channelId)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSmallIcon(smallIcon)

        val inboxStyle = NotificationCompat.InboxStyle()
        if (lines.isNotEmpty()) {
            for (element in lines) {
                inboxStyle.addLine(element)
            }
            inboxStyle.setBigContentTitle(bigContentTitle)
                .setSummaryText(summaryText)
        }
        return builder.setStyle(inboxStyle)
            .setGroup(groupKey)
            .setGroupSummary(true)
    }
}