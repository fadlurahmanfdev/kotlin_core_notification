package com.github.fadlurahmanfdev.core_notification_example.usecase

import android.content.Context

interface ExampleNotificationUseCase {
    fun askPermissionNotification(context: Context, onCompleteCheckPermission: (Boolean) -> Unit)
    fun showBasicNotification(context: Context)
    fun showImageNotification(context: Context)
    fun showInboxNotification(context: Context)
    fun showConversationNotification(context: Context)
}