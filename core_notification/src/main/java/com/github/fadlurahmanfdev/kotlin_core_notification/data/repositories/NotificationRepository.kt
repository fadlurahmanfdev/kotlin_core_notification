package com.github.fadlurahmanfdev.kotlin_core_notification.data.repositories

import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemConversationNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemGroupedNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemInboxNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemPerson

interface NotificationRepository {
    /**
     * Determine whether you have been granted a particular permission.
     * @return isGranted: return true if permission is [PackageManager.PERMISSION_GRANTED] and return false if
     * permission is [PackageManager.PERMISSION_DENIED].
     */
    fun askNotificationPermission(
        context: Context,
        onCompleteCheckPermission: (isGranted: Boolean) -> Unit
    )

    /**
     * Determine whether you have been granted a notification permission.
     * @return return true if permission is [PackageManager.PERMISSION_GRANTED] and return false if
     * permission is [PackageManager.PERMISSION_DENIED].
     */
    fun isNotificationPermissionGranted(context: Context): Boolean

    fun isNotificationChannelExist(context: Context, channelId: String): Boolean

    fun createNotificationChannel(
        context: Context,
        channelId: String,
        channelName: String,
        channelDescription: String,
        sound: Uri,
    )

    fun deleteNotificationChannel(
        context: Context,
        channelId: String,
    )

    fun showBasicNotification(
        context: Context,
        id: Int,
        title: String,
        message: String,
        @DrawableRes smallIcon: Int,
        groupKey: String?,
        pendingIntent: PendingIntent?,
    )

    fun showBasicImageNotification(
        context: Context,
        id: Int,
        title: String,
        message: String,
        imageUrl: String,
        @DrawableRes smallIcon: Int,
        pendingIntent: PendingIntent?,
    )

    fun showBasicInboxNotification(
        context: Context,
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
        context: Context,
        id: Int,
        channelId: String,
        title: String,
        text: String,
        @DrawableRes smallIcon: Int,
        inboxes: List<ItemInboxNotificationModel>
    )

    fun showBasicConversationNotification(
        context: Context,
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
        context: Context,
        id: Int,
        channelId: String,
        @DrawableRes smallIcon: Int,
        conversationTitle: String,
        conversationFrom: ItemPerson,
        conversations: List<ItemConversationNotificationModel>,
    )

    @Deprecated("not ready yet")
    fun showGroupedNotification(
        context: Context,
        id: Int,
        channelId: String,
        groupKey: String,
        bigContentTitle: String,
        summaryText: String,
        @DrawableRes smallIcon: Int,
        itemLine: List<String>,
        itemNotifications: List<ItemGroupedNotificationModel>,
    )

    fun showCustomNotification(
        context: Context,
        id: Int,
        notificationBuilder: NotificationCompat.Builder,
    )

    fun cancelNotification(context: Context, id: Int)
}