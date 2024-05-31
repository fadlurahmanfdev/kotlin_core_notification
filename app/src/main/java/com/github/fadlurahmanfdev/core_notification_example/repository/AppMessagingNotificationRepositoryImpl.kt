package com.github.fadlurahmanfdev.core_notification_example.repository

import android.content.Context
import androidx.annotation.DrawableRes
import com.github.fadlurahmanfdev.core_notification_example.R
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemConversationNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemInboxNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemPerson
import com.github.fadlurahmanfdev.kotlin_core_notification.others.BaseMessagingNotificationService

class AppMessagingNotificationRepositoryImpl(context: Context) : BaseMessagingNotificationService(context),
    AppMessagingNotificationRepository {

    companion object {
        @DrawableRes
        val BANK_MAS_LOGO_ICON = R.drawable.il_logo_bankmas
    }

    override fun showInboxesNotification(
        id: Int,
        title: String,
        message: String,
        inboxesItem: List<ItemInboxNotificationModel>
    ) {
        showInboxNotification(
            notificationId = id,
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
    }
}