package com.github.fadlurahmanfdev.kotlin_core_notification.data.repositories

import android.app.Notification
import android.app.PendingIntent
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemConversationNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemGroupedNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemInboxNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemPerson

interface NotificationRepository {
    fun isSupportedNotificationChannel(): Boolean

    fun isNotificationPermissionEnabledAndGranted(): Boolean

    fun isNotificationChannelExist(channelId: String): Boolean

    fun createNotificationChannel(
        channelId: String,
        channelName: String,
        channelDescription: String,
        sound: Uri?,
    ): Boolean

    fun deleteNotificationChannel(channelId: String):Boolean

    fun showNotification(
        notificationId: Int,
        title: String,
        message: String,
        @DrawableRes smallIcon: Int,
        groupKey: String?,
        pendingIntent: PendingIntent?,
    )

    fun showCustomNotification(
        channelId: String,
        notificationId: Int,
        title: String,
        message: String,
        @DrawableRes smallIcon: Int,
        groupKey: String?,
        pendingIntent: PendingIntent?,
    )

    fun showCustomNotification(
        notificationId: Int,
        notificationBuilder: NotificationCompat.Builder,
    )

    fun showImageNotification(
        notificationId: Int,
        title: String,
        message: String,
        imageUrl: String,
        @DrawableRes smallIcon: Int,
        pendingIntent: PendingIntent?,
    )

    fun showImageNotification(
        notificationId: Int,
        title: String,
        message: String,
        image: Bitmap,
        @DrawableRes smallIcon: Int,
        pendingIntent: PendingIntent?,
    )

    fun showCustomImageNotification(
        channelId: String,
        notificationId: Int,
        title: String,
        message: String,
        imageUrl: String,
        @DrawableRes smallIcon: Int,
        pendingIntent: PendingIntent?,
    )

    fun showCustomImageNotification(
        channelId: String,
        notificationId: Int,
        title: String,
        message: String,
        image: Bitmap,
        @DrawableRes smallIcon: Int,
        pendingIntent: PendingIntent?,
    )

    fun showInboxNotification(
        notificationId: Int,
        title: String,
        text: String,
        @DrawableRes smallIcon: Int,
        inboxes: List<ItemInboxNotificationModel>
    )

    /**
     * don't forget to create channel with same channelId before using [NotificationRepository.createNotificationChannel]
     * */
    fun showCustomInboxNotification(
        notificationId: Int,
        channelId: String,
        title: String,
        text: String,
        @DrawableRes smallIcon: Int,
        inboxes: List<ItemInboxNotificationModel>
    )

    fun showConversationNotification(
        notificationId: Int,
        @DrawableRes smallIcon: Int,
        conversationTitle: String,
        conversationFrom: ItemPerson,
        conversations: List<ItemConversationNotificationModel>,
    )

    /**
     * don't forget to create channel with same channelId before using [NotificationRepository.createNotificationChannel]
     * */
    fun showCustomConversationNotification(
        notificationId: Int,
        channelId: String,
        @DrawableRes smallIcon: Int,
        conversationTitle: String,
        conversationFrom: ItemPerson,
        conversations: List<ItemConversationNotificationModel>,
    )

    @Deprecated("not ready yet")
    fun showGroupedNotification(
        notificationId: Int,
        channelId: String,
        groupKey: String,
        bigContentTitle: String,
        summaryText: String,
        @DrawableRes smallIcon: Int,
        itemLine: List<String>,
        itemNotifications: List<ItemGroupedNotificationModel>,
    )

    fun showNotification(notificationId: Int, notification: Notification)
    fun cancelNotification(notificationId: Int)
}