package com.github.fadlurahmanfdev.core_notification_example.repository

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.annotation.DrawableRes
import com.github.fadlurahmanfdev.core_notification.data.dto.model.ItemMessagingNotificationModel
import com.github.fadlurahmanfdev.core_notification.data.repositories.NotificationRepository
import com.github.fadlurahmanfdev.core_notification.data.repositories.NotificationRepositoryImpl
import com.github.fadlurahmanfdev.core_notification_example.R

class AppNotificationRepositoryImpl (
    private val notificationRepository: NotificationRepository,
) : AppNotificationRepository {
    companion object {
        @DrawableRes
        val BANK_MAS_LOGO_ICON = R.drawable.il_logo_bankmas
        const val GENERAL_CHANNEL_ID = NotificationRepositoryImpl.GENERAL_CHANNEL_ID
        const val GENERAL_CHANNEL_NAME = NotificationRepositoryImpl.GENERAL_CHANNEL_NAME
        const val GENERAL_CHANNEL_DESCRIPTION =
            NotificationRepositoryImpl.GENERAL_CHANNEL_DESCRIPTION
        const val MESSAGING_CHANNEL_ID = "MESSAGING"
        const val MESSAGING_CHANNEL_NAME = "Percakapan"
        const val MESSAGING_CHANNEL_DESCRIPTION =
            "Notifikasi Percakapan"
        const val VOIP_CHANNEL_ID = "VOIP"
        const val VOIP_CHANNEL_NAME = "Panggilan"
        const val VOIP_CHANNEL_DESCRIPTION =
            "Notifikasi Panggilan"
    }

    private fun createGeneralChannel(context: Context) {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        notificationRepository.createNotificationChannel(
            context,
            channelId = GENERAL_CHANNEL_ID,
            channelName = GENERAL_CHANNEL_NAME,
            channelDescription = GENERAL_CHANNEL_DESCRIPTION,
            sound = sound,
        )
    }

    private fun createMessagingChannel(context: Context) {
        val sound =
            Uri.parse("android.resource://" + context.packageName + "/" + R.raw.messaging_notification_sound)
        notificationRepository.createNotificationChannel(
            context,
            channelId = MESSAGING_CHANNEL_ID,
            channelName = MESSAGING_CHANNEL_NAME,
            channelDescription = MESSAGING_CHANNEL_DESCRIPTION,
            sound = sound,
        )
    }

    override fun createVOIPChannel(context: Context) {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        notificationRepository.createNotificationChannel(
            context,
            channelId = VOIP_CHANNEL_ID,
            channelName = VOIP_CHANNEL_NAME,
            channelDescription = VOIP_CHANNEL_DESCRIPTION,
            sound = sound,
        )
    }

    override fun askPermission(
        context: Context,
        onCompleteCheckPermission: (isGranted: Boolean) -> Unit
    ) {
        notificationRepository.askNotificationPermission(context, onCompleteCheckPermission)
    }

    override fun showNotification(context: Context, id: Int, title: String, message: String) {
        askPermission(context, onCompleteCheckPermission = { isGranted ->
            if (isGranted) {
                createGeneralChannel(context)
                notificationRepository.showGeneralNotification(
                    context,
                    id = id,
                    title = title,
                    message = message,
                    smallIcon = BANK_MAS_LOGO_ICON,
                    groupKey = null,
                    pendingIntent = null
                )
            } else {
                Log.d(
                    AppNotificationRepositoryImpl::class.java.simpleName,
                    "unable to showNotification cause permission is not granted"
                )
            }
        })
    }

    override fun showMessagingNotification(context: Context, id: Int) {
        askPermission(context, onCompleteCheckPermission = { isGranted ->
            if (isGranted) {
                createMessagingChannel(context)
                val messages = listOf(
                    ItemMessagingNotificationModel(
                        message = "MESSAGE FROM PERSON 1",
                        messageFrom = "PERSON 1",
                        personImageFromNetwork = "https://raw.githubusercontent.com/TutorialsBuzz/cdn/main/android.jpg",
                        timestamp = System.currentTimeMillis(),
                    ),
                    ItemMessagingNotificationModel(
                        message = "MESSAGE FROM PERSON 2",
                        messageFrom = "PERSON 2",
                        personImageFromNetwork = "https://picsum.photos/400",
                        timestamp = System.currentTimeMillis()
                    ),
                    ItemMessagingNotificationModel(
                        message = "MESSAGE FROM PERSON 3",
                        messageFrom = "PERSON 3",
                        personImageFromNetwork = "https://raw.githubusercontent.com/TutorialsBuzz/cdn/main/android.jpg",
                        timestamp = System.currentTimeMillis(),
                    ),
                )
                notificationRepository.showMessagingNotification(
                    context,
                    id = id,
                    channelId = MESSAGING_CHANNEL_ID,
                    groupKey = "${context.packageName}.MESSAGING",
                    items = messages,
                    smallIcon = BANK_MAS_LOGO_ICON,
                )
            } else {
                Log.d(
                    AppNotificationRepositoryImpl::class.java.simpleName,
                    "unable to showMessagingNotification cause permission is not granted"
                )
            }
        })
    }
}