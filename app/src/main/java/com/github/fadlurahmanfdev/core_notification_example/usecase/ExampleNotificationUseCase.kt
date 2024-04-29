package com.github.fadlurahmanfdev.core_notification_example.usecase

import android.content.Context

interface ExampleNotificationUseCase {
    fun askPermissionNotification(context: Context, onCompleteCheckPermission: (Boolean) -> Unit)
    fun showSimpleNotification(context: Context)
    fun showMessagingNotification(context: Context)
}