package com.github.fadlurahmanfdev.kotlin_core_notification.data.repositories

import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
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

    /**
     * Determine whether you have been granted a notification permission.
     * @return return true if permission is [PackageManager.PERMISSION_GRANTED] and return false if
     * permission is [PackageManager.PERMISSION_DENIED].
     */
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
        id: Int,
        title: String,
        message: String,
        @DrawableRes smallIcon: Int,
        groupKey: String?,
        pendingIntent: PendingIntent?,
    )

    fun showCustomNotification(
        channelId: String,
        id: Int,
        title: String,
        message: String,
        @DrawableRes smallIcon: Int,
        groupKey: String?,
        pendingIntent: PendingIntent?,
    )

    fun showCustomNotification(
        id: Int,
        notificationBuilder: NotificationCompat.Builder,
    )

    fun showImageNotification(
        id: Int,
        title: String,
        message: String,
        imageUrl: String,
        @DrawableRes smallIcon: Int,
        pendingIntent: PendingIntent?,
    )

    fun showImageNotification(
        id: Int,
        title: String,
        message: String,
        image: Bitmap,
        @DrawableRes smallIcon: Int,
        pendingIntent: PendingIntent?,
    )

    fun showCustomImageNotification(
        channelId: String,
        id: Int,
        title: String,
        message: String,
        imageUrl: String,
        @DrawableRes smallIcon: Int,
        pendingIntent: PendingIntent?,
    )

    fun showCustomImageNotification(
        channelId: String,
        id: Int,
        title: String,
        message: String,
        image: Bitmap,
        @DrawableRes smallIcon: Int,
        pendingIntent: PendingIntent?,
    )

    fun showInboxNotification(
        id: Int,
        title: String,
        text: String,
        @DrawableRes smallIcon: Int,
        inboxes: List<ItemInboxNotificationModel>
    )

    /**
     * don't forget to create channel with same channelId before using [NotificationRepository.createNotificationChannel]
     * */
    fun showCustomInboxNotification(
        id: Int,
        channelId: String,
        title: String,
        text: String,
        @DrawableRes smallIcon: Int,
        inboxes: List<ItemInboxNotificationModel>
    )

    fun showConversationNotification(
        id: Int,
        @DrawableRes smallIcon: Int,
        conversationTitle: String,
        conversationFrom: ItemPerson,
        conversations: List<ItemConversationNotificationModel>,
    )

    /**
     * don't forget to create channel with same channelId before using [NotificationRepository.createNotificationChannel]
     * */
    fun showCustomConversationNotification(
        id: Int,
        channelId: String,
        @DrawableRes smallIcon: Int,
        conversationTitle: String,
        conversationFrom: ItemPerson,
        conversations: List<ItemConversationNotificationModel>,
    )

    @Deprecated("not ready yet")
    fun showGroupedNotification(
        id: Int,
        channelId: String,
        groupKey: String,
        bigContentTitle: String,
        summaryText: String,
        @DrawableRes smallIcon: Int,
        itemLine: List<String>,
        itemNotifications: List<ItemGroupedNotificationModel>,
    )

    fun cancelNotification(context: Context, id: Int)
}