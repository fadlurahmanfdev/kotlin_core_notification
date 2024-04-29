package com.github.fadlurahmanfdev.kotlin_core_notification.others

import android.app.NotificationManager
import android.content.Context
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