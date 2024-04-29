package com.github.fadlurahmanfdev.core_notification_example.usecase

import android.content.Context
import com.github.fadlurahmanfdev.core_notification_example.repository.AppNotificationRepository

class ExampleNotificationUseCaseImpl (
    private val appNotificationRepository: AppNotificationRepository
) : ExampleNotificationUseCase {

    override fun askPermissionNotification(
        context: Context,
        onCompleteCheckPermission: (Boolean) -> Unit
    ) {
        appNotificationRepository.askPermission(
            context,
            onCompleteCheckPermission = onCompleteCheckPermission
        )
    }

    override fun showSimpleNotification(context: Context) =
        appNotificationRepository.showNotification(
            context,
            id = 1,
            title = "Simple Notification",
            message = "This is Example of Simple Notification"
        )

    override fun showMessagingNotification(context: Context) =
        appNotificationRepository.showMessagingNotification(context, 2)

}
