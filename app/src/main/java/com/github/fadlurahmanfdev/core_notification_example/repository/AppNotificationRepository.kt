package com.github.fadlurahmanfdev.core_notification_example.repository

import android.content.Context

interface AppNotificationRepository {
    fun askPermission(context: Context, onCompleteCheckPermission: (isGranted: Boolean) -> Unit)
    fun createVOIPChannel(context: Context)
    fun showNotification(context: Context, id: Int, title: String, message: String)
    fun showMessagingNotification(context: Context, id: Int)
}