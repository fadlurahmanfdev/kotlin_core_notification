package com.github.fadlurahmanfdev.kotlin_core_notification.others

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

abstract class BaseNotificationService(val context: Context) {
    var notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    /**
     * check whether notification permission is granted & enabled
     * return true if notification permission is granted, otherwise it will return false
     * */
    open fun isNotificationPermissionEnabledAndGranted(): Boolean {
        return when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU -> {
                val isNotificationEnabled =
                    NotificationManagerCompat.from(context).areNotificationsEnabled()
                isNotificationEnabled
            }

            else -> {
                val status = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission_group.NOTIFICATIONS
                )
                status == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    open fun isSupportedNotificationChannel(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    /**
     * create notification channel
     * if notification channel is successfully created, it will return true, otherwise it will return false
     * */
    open fun createNotificationChannel(
        channelId: String,
        channelName: String,
        channelDescription: String,
        sound: Uri,
    ): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (isNotificationChannelExist(channelId)) {
                return true
            } else {
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = channelDescription
                    setSound(sound, null)
                }
                notificationManager.createNotificationChannel(channel)
                return isNotificationChannelExist(channelId)
            }
        } else {
            Log.i(
                BaseNotificationService::class.java.simpleName,
                "${Build.VERSION.SDK_INT} is not supported to create notification channel"
            )
            return true
        }
    }

    /**
     * check whether is notification channel is exist
     * return true if notification channel is exist
     * */
    open fun isNotificationChannelExist(channelId: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val allChannels = notificationManager.notificationChannels
            var knownChannel: NotificationChannel? = null
            for (element in allChannels) {
                if (element.id == channelId) {
                    knownChannel = element
                    break
                }
            }
            return knownChannel != null
        } else {
            Log.i(
                BaseNotificationService::class.java.simpleName,
                "${Build.VERSION.SDK_INT} is not supported to get notification channel"
            )
            return true
        }
    }


    /**
     * delete notification channel
     * return true if notification channel is successfully deleted
     * */
    open fun deleteNotificationChannel(channelId: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (isNotificationChannelExist(channelId)) {
                notificationManager.deleteNotificationChannel(channelId)
                return !isNotificationChannelExist(channelId)
            }
            return true
        } else {
            Log.i(
                BaseNotificationService::class.java.simpleName,
                "${Build.VERSION.SDK_INT} is not supported to delete notification channel"
            )
            return true
        }
    }

    fun notificationBuilder(
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