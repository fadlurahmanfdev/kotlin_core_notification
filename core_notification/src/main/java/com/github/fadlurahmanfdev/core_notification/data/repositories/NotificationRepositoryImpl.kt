package com.github.fadlurahmanfdev.core_notification.data.repositories

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
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.fadlurahmanfdev.core_notification.data.dto.model.ItemGroupedNotificationModel
import com.github.fadlurahmanfdev.core_notification.data.dto.model.ItemMessagingNotificationModel
import com.github.fadlurahmanfdev.core_notification.others.BaseNotificationService

class NotificationRepositoryImpl : BaseNotificationService(),
    NotificationRepository {

    companion object {
        const val GENERAL_CHANNEL_ID = "GENERAL"
        const val GENERAL_CHANNEL_NAME = "Umum"
        const val GENERAL_CHANNEL_DESCRIPTION = "Notifikasi Umum"
    }

    override fun askNotificationPermission(
        context: Context,
        onCompleteCheckPermission: (isGranted: Boolean) -> Unit
    ) {
        when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU -> {
                val isNotificationEnabled =
                    NotificationManagerCompat.from(context).areNotificationsEnabled()
                onCompleteCheckPermission(isNotificationEnabled)
            }

            else -> {
                val status = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission_group.NOTIFICATIONS
                )
                onCompleteCheckPermission(status == PackageManager.PERMISSION_GRANTED)
            }
        }
    }

    override fun isNotificationChannelExist(context: Context, channelId: String): Boolean {
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

    override fun createNotificationChannel(
        context: Context,
        channelId: String,
        channelName: String,
        channelDescription: String,
        sound: Uri,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!isNotificationChannelExist(context, channelId)) {
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

    override fun deleteNotificationChannel(context: Context, channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (isNotificationChannelExist(context, channelId)) {
                getNotificationManager(context).deleteNotificationChannel(channelId)
            }
        }
    }

    override fun showGeneralNotification(
        context: Context,
        id: Int,
        title: String,
        message: String,
        @DrawableRes smallIcon: Int,
        groupKey: String?,
        pendingIntent: PendingIntent?,
    ) {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        createNotificationChannel(
            context,
            GENERAL_CHANNEL_ID,
            GENERAL_CHANNEL_NAME,
            GENERAL_CHANNEL_DESCRIPTION,
            sound,
        )
        val notification =
            notificationBuilder(context, GENERAL_CHANNEL_ID, smallIcon).apply {
                setContentTitle(title)
                setContentText(message)
                if (groupKey != null) {
                    setGroup(groupKey)
                }
                if (pendingIntent != null) {
                    setContentIntent(pendingIntent)
                }
            }
        getNotificationManager(context).notify(id, notification.build())
    }

    override fun showGeneralImageNotification(
        context: Context,
        id: Int,
        title: String,
        message: String,
        imageUrl: String,
        @DrawableRes smallIcon: Int,
    ) {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        createNotificationChannel(
            context,
            GENERAL_CHANNEL_ID,
            GENERAL_CHANNEL_NAME,
            GENERAL_CHANNEL_DESCRIPTION,
            sound,
        )
        val notification = notificationBuilder(context, GENERAL_CHANNEL_ID, smallIcon).apply {
            setContentTitle(title)
            setContentText(message)
        }
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    notification.setLargeIcon(resource)
                    notification.setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                    getNotificationManager(context)
                        .notify(id, notification.build())
                }

                override fun onLoadCleared(placeholder: Drawable?) {}

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    Log.e(NotificationRepositoryImpl::class.java.simpleName, "failed onLoadFailed")
                    getNotificationManager(context)
                        .notify(id, notification.build())
                }

            })
    }

    override fun showCustomNotification(
        context: Context,
        id: Int,
        channelId: String,
        title: String,
        message: String,
        @DrawableRes smallIcon: Int,
        groupKey: String?,
        pendingIntent: PendingIntent?,
    ) {
        val notification =
            notificationBuilder(context, channelId, smallIcon).apply {
                setContentTitle(title)
                setContentText(message)
                if (pendingIntent != null) {
                    setContentIntent(pendingIntent)
                }
                if (groupKey != null) {
                    setGroup(groupKey)
                }
            }
        getNotificationManager(context).notify(id, notification.build())
    }

    override fun cancelNotification(context: Context, id: Int) {
        getNotificationManager(context).cancel(id)
    }

    override fun showGroupedNotification(
        context: Context,
        id: Int,
        channelId: String,
        groupKey: String,
        bigContentTitle: String,
        summaryText: String,
        @DrawableRes smallIcon: Int,
        itemLine: List<String>,
        itemNotifications: List<ItemGroupedNotificationModel>,
    ) {
        val notifications: ArrayList<Notification> = arrayListOf()
        for (index in itemNotifications.indices) {
            notifications.add(
                notificationBuilder(
                    context,
                    channelId = channelId,
                    smallIcon = smallIcon
                ).apply {
                    setContentTitle(itemNotifications[index].title)
                    setContentText(itemNotifications[index].message)
                    setGroup(groupKey)
                }.build()
            )
        }
        val builder = groupNotificationBuilder(
            context,
            channelId = channelId,
            groupKey = groupKey,
            bigContentTitle = bigContentTitle,
            summaryText = summaryText,
            lines = itemLine,
            smallIcon = smallIcon,
        )
        getNotificationManager(context).apply {
            for (index in itemNotifications.indices) {
                notify(itemNotifications[index].id, notifications[index])
            }
            notify(id, builder.build())
        }
    }

    private fun getPersonFromItemMessaging(
        context: Context,
        item: ItemMessagingNotificationModel,
        onComplete: (Person.Builder) -> Unit
    ) {
        val personBuilder = Person.Builder().setName(item.messageFrom)
        if (item.personImageFromNetwork != null) {
            Glide.with(context)
                .asBitmap()
                .load(item.personImageFromNetwork)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        personBuilder.setIcon(IconCompat.createWithBitmap(resource))
                        onComplete(personBuilder)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        onComplete(personBuilder)
                    }

                })
        }
    }

    override fun showMessagingNotification(
        context: Context,
        id: Int,
        channelId: String,
        groupKey: String,
        items: List<ItemMessagingNotificationModel>,
        smallIcon: Int,
    ) {
        val builder = notificationBuilder(
            context,
            channelId = channelId,
            smallIcon = smallIcon,
        )

        var person1Builder: Person.Builder
        val totalMessages = items.size

        getPersonFromItemMessaging(
            context, item = items.first(),
            onComplete = { personBuilder ->
                person1Builder = personBuilder

                val messagingStyle = NotificationCompat.MessagingStyle(
                    person1Builder.build()
                )

                var counter = 0
                for (element in items) {
                    getPersonFromItemMessaging(
                        context, item = element,
                        onComplete = { personBuilderStep2 ->
                            counter++
                            messagingStyle.addMessage(
                                element.message,
                                element.timestamp,
                                personBuilderStep2.build()
                            )

                            if (counter >= totalMessages) {
                                messagingStyle.setGroupConversation(true)

                                builder.setStyle(messagingStyle)
                                    .setGroup(groupKey)
                                    .setGroupSummary(true)

                                getNotificationManager(context).notify(id, builder.build())
                            }
                        },
                    )
                }

            },
        )
    }
}