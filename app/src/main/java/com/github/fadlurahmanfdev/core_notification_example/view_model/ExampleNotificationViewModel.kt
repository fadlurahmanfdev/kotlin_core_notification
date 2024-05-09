package com.github.fadlurahmanfdev.core_notification_example.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.fadlurahmanfdev.core_notification_example.usecase.ExampleNotificationUseCase

class ExampleNotificationViewModel(
    private val exampleNotificationUseCase: ExampleNotificationUseCase
) : ViewModel() {

    fun askPermission(context: Context) {
        exampleNotificationUseCase.askPermissionNotification(
            context,
            onCompleteCheckPermission = { isGranted ->
                Log.d(
                    ExampleNotificationViewModel::class.java.simpleName,
                    "IS NOTIFICATION PERMISSION GRANTED: $isGranted"
                )
            }
        )
    }

    fun showNotification(context: Context) =
        exampleNotificationUseCase.showBasicNotification(context)

    fun showImageNotification(context: Context) =
        exampleNotificationUseCase.showImageNotification(context)

    fun showInboxNotification(context: Context) =
        exampleNotificationUseCase.showInboxNotification(context)

    fun showConversationNotification(context: Context) {
        exampleNotificationUseCase.showConversationNotification(context)
    }
}