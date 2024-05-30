package com.github.fadlurahmanfdev.core_notification_example.repository

import android.content.Context
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemConversationNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemInboxNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemPerson

interface AppNotificationRepository {
    fun isPermissionEnabledAndGranted(): Boolean
    fun createVOIPChannel(context: Context)
    fun showNotification(context: Context, id: Int, title: String, message: String)
    fun showImageNotification(
        id: Int,
        title: String,
        message: String,
        imageUrl: String,
    )

    fun showCustomImageNotification(
        context: Context,
        id: Int,
        title: String,
        message: String,
        imageUrl: String,
    )

    fun showInboxesNotification(
        context: Context,
        id: Int,
        title: String,
        message: String,
        inboxesItem: List<ItemInboxNotificationModel>
    )

    fun showConversationNotification(
        context: Context,
        id: Int,
        conversationTitle: String,
        from: ItemPerson,
        conversations: List<ItemConversationNotificationModel>
    )
}