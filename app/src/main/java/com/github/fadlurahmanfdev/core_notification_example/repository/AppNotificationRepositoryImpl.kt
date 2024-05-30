package com.github.fadlurahmanfdev.core_notification_example.repository

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import androidx.annotation.DrawableRes
import com.github.fadlurahmanfdev.core_notification_example.R
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemConversationNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemInboxNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemPerson
import com.github.fadlurahmanfdev.kotlin_core_notification.data.repositories.NotificationRepository
import com.github.fadlurahmanfdev.kotlin_core_notification.data.repositories.NotificationRepositoryImpl

class AppNotificationRepositoryImpl(
    private val notificationRepository: NotificationRepository,
) : AppNotificationRepository {
    companion object {
        @DrawableRes
        val BANK_MAS_LOGO_ICON = R.drawable.il_logo_bankmas
        const val GENERAL_CHANNEL_ID = NotificationRepositoryImpl.GENERAL_CHANNEL_ID
        const val GENERAL_CHANNEL_NAME = NotificationRepositoryImpl.GENERAL_CHANNEL_NAME
        const val GENERAL_CHANNEL_DESCRIPTION =
            NotificationRepositoryImpl.GENERAL_CHANNEL_DESCRIPTION
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
        notificationRepository.isNotificationPermissionEnabledAndGranted()
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        notificationRepository.createNotificationChannel(
            channelId = GENERAL_CHANNEL_ID,
            channelName = GENERAL_CHANNEL_NAME,
            channelDescription = GENERAL_CHANNEL_DESCRIPTION,
            sound = sound,
        )
    }

    private fun createConversationChannel(context: Context) {
        val sound =
            Uri.parse("android.resource://" + context.packageName + "/" + R.raw.messaging_notification_sound)
        notificationRepository.createNotificationChannel(
            channelId = CONVERSATION_CHANNEL_ID,
            channelName = CONVERSATION_CHANNEL_NAME,
            channelDescription = CONVERSATION_CHANNEL_DESCRIPTION,
            sound = sound,
        )
    }

    override fun createVOIPChannel(context: Context) {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        notificationRepository.createNotificationChannel(
            channelId = VOIP_CHANNEL_ID,
            channelName = VOIP_CHANNEL_NAME,
            channelDescription = VOIP_CHANNEL_DESCRIPTION,
            sound = sound,
        )
    }

    private fun createCustomChannel(context: Context) {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        notificationRepository.createNotificationChannel(
            channelId = CUSTOM_CHANNEL_ID,
            channelName = CUSTOM_CHANNEL_NAME,
            channelDescription = CUSTOM_CHANNEL_DESCRIPTION,
            sound = sound,
        )
    }

    override fun isPermissionEnabledAndGranted(): Boolean {
        return notificationRepository.isNotificationPermissionEnabledAndGranted()
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
        notificationRepository.showImageNotification(
            id = id,
            title = title,
            message = message,
            smallIcon = BANK_MAS_LOGO_ICON,
            imageUrl = imageUrl,
            pendingIntent = null,
        )
    }

    override fun showCustomImageNotification(
        context: Context,
        id: Int,
        title: String,
        message: String,
        imageUrl: String
    ) {
        isPermissionEnabledAndGranted()
    }

    override fun showInboxesNotification(
        context: Context,
        id: Int,
        title: String,
        message: String,
        inboxesItem: List<ItemInboxNotificationModel>
    ) {
        notificationRepository.showInboxNotification(
            id = id,
            title = title,
            text = message,
            smallIcon = BANK_MAS_LOGO_ICON,
            inboxes = inboxesItem
        )
    }

    override fun showConversationNotification(
        context: Context,
        id: Int,
        conversationTitle: String,
        from: ItemPerson,
        conversations: List<ItemConversationNotificationModel>
    ) {
        isPermissionEnabledAndGranted()
    }
}