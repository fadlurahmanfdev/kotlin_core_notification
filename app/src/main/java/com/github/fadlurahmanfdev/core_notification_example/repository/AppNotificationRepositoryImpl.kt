package com.github.fadlurahmanfdev.core_notification_example.repository

import android.content.Context
import android.media.RingtoneManager
import androidx.annotation.DrawableRes
import com.github.fadlurahmanfdev.core_notification_example.R
import com.github.fadlurahmanfdev.kotlin_core_notification.others.BaseNotificationService

class AppNotificationRepositoryImpl(context: Context) : BaseNotificationService(context),
    AppNotificationRepository {
    companion object {
        @DrawableRes
        val BANK_MAS_LOGO_ICON = R.drawable.il_logo_bankmas
        const val GENERAL_CHANNEL_ID = BaseNotificationService.GENERAL_CHANNEL_ID
        const val GENERAL_CHANNEL_NAME = BaseNotificationService.GENERAL_CHANNEL_NAME
        const val GENERAL_CHANNEL_DESCRIPTION =
            BaseNotificationService.GENERAL_CHANNEL_DESCRIPTION
        const val CONVERSATION_CHANNEL_ID = "CONVERSATION"
        const val CONVERSATION_CHANNEL_NAME = "Percakapan"
        const val CONVERSATION_CHANNEL_DESCRIPTION =
            "Notifikasi Percakapan"
        const val VOIP_CHANNEL_ID = "VOIP"
        const val VOIP_CHANNEL_NAME = "Panggilan"
        const val VOIP_CHANNEL_DESCRIPTION =
            "Notifikasi Panggilan"
        const val CUSTOM_CHANNEL_ID = "CUSTOM"
        const val CUSTOM_CHANNEL_NAME = "Custom"
        const val CUSTOM_CHANNEL_DESCRIPTION =
            "Notifikasi Custom"
    }

    private fun createGeneralChannel(context: Context) {
        if (isNotificationPermissionEnabledAndGranted()) {
            val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            createNotificationChannel(
                channelId = GENERAL_CHANNEL_ID,
                channelName = GENERAL_CHANNEL_NAME,
                channelDescription = GENERAL_CHANNEL_DESCRIPTION,
                sound = sound,
            )
        }
    }

    private fun createConversationChannel(context: Context) {
        if (isNotificationPermissionEnabledAndGranted()) {
            val sound = getUriSoundFromResource(context, R.raw.messaging_notification_sound)
            createNotificationChannel(
                channelId = CONVERSATION_CHANNEL_ID,
                channelName = CONVERSATION_CHANNEL_NAME,
                channelDescription = CONVERSATION_CHANNEL_DESCRIPTION,
                sound = sound,
            )
        }
    }

    override fun createVOIPChannel(context: Context) {
        if (isNotificationPermissionEnabledAndGranted()) {
            val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            createNotificationChannel(
                channelId = VOIP_CHANNEL_ID,
                channelName = VOIP_CHANNEL_NAME,
                channelDescription = VOIP_CHANNEL_DESCRIPTION,
                sound = sound,
            )
        }
    }

    private fun createCustomChannel(context: Context) {
        if (isNotificationPermissionEnabledAndGranted()) {
            val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            createNotificationChannel(
                channelId = CUSTOM_CHANNEL_ID,
                channelName = CUSTOM_CHANNEL_NAME,
                channelDescription = CUSTOM_CHANNEL_DESCRIPTION,
                sound = sound,
            )
        }
    }

    override fun isPermissionEnabledAndGranted(): Boolean {
        return super.isNotificationPermissionEnabledAndGranted()
    }

    override fun showNotification(context: Context, id: Int, title: String, message: String) {
        isPermissionEnabledAndGranted()
    }

    override fun showImageNotification(
        id: Int,
        title: String,
        message: String,
        imageUrl: String,
    ) {
        showImageNotification(
            notificationId = id,
            title = title,
            message = message,
            smallIcon = BANK_MAS_LOGO_ICON,
            imageUrl = imageUrl,
            pendingIntent = null,
        )
    }

    override fun showCustomImageNotification(
        id: Int,
        title: String,
        message: String,
        imageUrl: String
    ) {
        return showCustomImageNotification(
            channelId = CUSTOM_CHANNEL_ID,
            notificationId = id,
            title = title,
            message = message,
            imageUrl = imageUrl,
            smallIcon = BANK_MAS_LOGO_ICON,
            pendingIntent = null
        )
    }
}