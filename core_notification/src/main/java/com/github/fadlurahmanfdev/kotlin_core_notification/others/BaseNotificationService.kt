package com.github.fadlurahmanfdev.kotlin_core_notification.others

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

abstract class BaseNotificationService(val context: Context) {
    var notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val GENERAL_CHANNEL_ID = "GENERAL"
        const val GENERAL_CHANNEL_NAME = "Umum"
        const val GENERAL_CHANNEL_DESCRIPTION = "Notifikasi Umum"
    }

    fun getUriSoundFromResource(context: Context, @RawRes soundNotification: Int): Uri {
        return Uri.parse("android.resource://" + context.packageName + "/" + soundNotification)
    }

    fun isCanShowGeneralNotification(): Boolean {
        if (!isNotificationPermissionEnabledAndGranted()) {
            Log.i(
                BaseNotificationService::class.java.simpleName,
                "permission notification is not granted"
            )
            return false
        }
        if (!isSupportedNotificationChannel()) return false
        if (isNotificationChannelExist(GENERAL_CHANNEL_ID)) return true
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        return createNotificationChannel(
            GENERAL_CHANNEL_ID,
            GENERAL_CHANNEL_NAME,
            GENERAL_CHANNEL_DESCRIPTION,
            sound,
        )
    }

    /**
     * Determine whether you have been granted a notification permission.
     * @author fadlurahmanfdev
     * @return return true if permission is [PackageManager.PERMISSION_GRANTED] and return false if
     * permission is [PackageManager.PERMISSION_DENIED].
     * @see BaseNotificationService.createNotificationChannel
     * @see BaseNotificationService.isSupportedNotificationChannel
     */
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

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
    open fun isSupportedNotificationChannel(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    /**
     * create notification channel
     * if notification channel is successfully created, it will return true, otherwise it will return false
     * @author fadlurahmanfdev
     * @return true if notification channel is created or device didn't support notification channel, and return false if notification channel is not successfully created
     * @param channelId unique identifier for different channelId created for the apps
     * @param channelName channel name will be shown to user in notification
     * @param channelDescription channel description will be shown to user in notification
     * */
    open fun createNotificationChannel(
        channelId: String,
        channelName: String,
        channelDescription: String,
        sound: Uri?,
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

    open fun showNotification(
        notificationId: Int,
        title: String,
        message: String,
        @DrawableRes smallIcon: Int,
        groupKey: String?,
        pendingIntent: PendingIntent?,
    ) {
        if (isCanShowGeneralNotification()) {
            val notificationBuilder =
                notificationBuilder(GENERAL_CHANNEL_ID, smallIcon).apply {
                    setContentTitle(title)
                    setContentText(message)
                    if (groupKey != null) {
                        setGroup(groupKey)
                    }
                    if (pendingIntent != null) {
                        setContentIntent(pendingIntent)
                    }
                }
            showNotification(
                notificationId = notificationId,
                notification = notificationBuilder.build()
            )
        }
    }

    open fun showImageNotification(
        notificationId: Int,
        title: String,
        message: String,
        imageUrl: String,
        @DrawableRes smallIcon: Int,
        pendingIntent: PendingIntent?,
    ) {
        if (isCanShowGeneralNotification()) {
            val notificationBuilder =
                notificationBuilder(GENERAL_CHANNEL_ID, smallIcon).apply {
                    setContentTitle(title)
                    setContentText(message)

                    if (pendingIntent != null) {
                        setContentIntent(pendingIntent)
                    }
                }
            Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        notificationBuilder.setLargeIcon(resource)
                        notificationBuilder.setStyle(
                            NotificationCompat.BigPictureStyle().bigPicture(resource)
                                .bigLargeIcon(resource)
                        )
                        showNotification(
                            notificationId = notificationId,
                            notification = notificationBuilder.build()
                        )
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        Log.e(
                            BaseNotificationService::class.java.simpleName,
                            "failed onLoadFailed"
                        )
                        showNotification(
                            notificationId = notificationId,
                            notification = notificationBuilder.build()
                        )
                    }

                })
        }
    }

    open fun showImageNotification(
        notificationId: Int,
        title: String,
        message: String,
        image: Bitmap,
        smallIcon: Int,
        pendingIntent: PendingIntent?
    ) {
        if (isCanShowGeneralNotification()) {
            val notificationBuilder =
                notificationBuilder(GENERAL_CHANNEL_ID, smallIcon).apply {
                    setContentTitle(title)
                    setContentText(message)

                    if (pendingIntent != null) {
                        setContentIntent(pendingIntent)
                    }
                }
            notificationBuilder.setLargeIcon(image)
            notificationBuilder.setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(image)
                    .bigLargeIcon(image)
            )
            showNotification(
                notificationId = notificationId,
                notification = notificationBuilder.build()
            )
        }
    }

    open fun showCustomImageNotification(
        channelId: String,
        notificationId: Int,
        title: String,
        message: String,
        imageUrl: String,
        @DrawableRes smallIcon: Int,
        pendingIntent: PendingIntent?,
    ) {
        if (isCanShowGeneralNotification()) {
            val notificationBuilder =
                notificationBuilder(channelId, smallIcon).apply {
                    setContentTitle(title)
                    setContentText(message)

                    if (pendingIntent != null) {
                        setContentIntent(pendingIntent)
                    }
                }
            Glide.with(context)
                .asBitmap()
                .load(imageUrl)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        notificationBuilder.setLargeIcon(resource)
                        notificationBuilder.setStyle(
                            NotificationCompat.BigPictureStyle().bigPicture(resource)
                                .bigLargeIcon(resource)
                        )
                        showNotification(
                            notificationId = notificationId,
                            notification = notificationBuilder.build()
                        )
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        Log.e(
                            BaseNotificationService::class.java.simpleName,
                            "failed onLoadFailed"
                        )
                        showNotification(
                            notificationId = notificationId,
                            notification = notificationBuilder.build()
                        )
                    }
                })
        }
    }

    fun showCustomImageNotification(
        channelId: String,
        notificationId: Int,
        title: String,
        message: String,
        image: Bitmap,
        smallIcon: Int,
        pendingIntent: PendingIntent?
    ) {
        if (isCanShowGeneralNotification()) {
            val notificationBuilder =
                notificationBuilder(channelId, smallIcon).apply {
                    setContentTitle(title)
                    setContentText(message)

                    if (pendingIntent != null) {
                        setContentIntent(pendingIntent)
                    }
                }
            notificationBuilder.setLargeIcon(image)
            notificationBuilder.setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(image)
                    .bigLargeIcon(image)
            )
            showNotification(
                notificationId = notificationId,
                notification = notificationBuilder.build()
            )
        }
    }

    open fun showNotification(
        channelId: String,
        notificationId: Int,
        title: String,
        message: String,
        @DrawableRes smallIcon: Int,
        groupKey: String?,
        pendingIntent: PendingIntent?,
    ) {
        val notificationBuilder =
            notificationBuilder(channelId, smallIcon).apply {
                setContentTitle(title)
                setContentText(message)
                if (groupKey != null) {
                    setGroup(groupKey)
                }
                if (pendingIntent != null) {
                    setContentIntent(pendingIntent)
                }
            }
        showNotification(
            notificationId = notificationId,
            notification = notificationBuilder.build()
        )
    }

    open fun showNotificationUsingNotificationBuilder(
        notificationId: Int,
        notificationBuilder: NotificationCompat.Builder,
    ) {
        showNotification(
            notificationId = notificationId,
            notification = notificationBuilder.build()
        )
    }

    open fun showNotification(notificationId: Int, notification: Notification) {
        notificationManager.notify(notificationId, notification)
    }

    open fun cancelNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }
}