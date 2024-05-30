package com.github.fadlurahmanfdev.core_notification_example.usecase

import android.content.Context
import com.github.fadlurahmanfdev.core_notification_example.repository.AppNotificationRepository
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemConversationNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemInboxNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemPerson

class ExampleNotificationUseCaseImpl(
    private val appNotificationRepository: AppNotificationRepository
) : ExampleNotificationUseCase {

    override fun askPermissionNotification(
        context: Context,
        onCompleteCheckPermission: (Boolean) -> Unit
    ) {
        appNotificationRepository.isPermissionEnabledAndGranted()
    }

    override fun showBasicNotification(context: Context) =
        appNotificationRepository.showNotification(
            context,
            id = 1,
            title = "Simple Notification",
            message = "This is example of simple notification"
        )

    override fun showImageNotification(context: Context) =
        appNotificationRepository.showImageNotification(
            id = 1,
            title = "Image Notificaction",
            message = "This is example of image notification",
            imageUrl = "https://raw.githubusercontent.com/TutorialsBuzz/cdn/main/android.jpg",
        )

    override fun showInboxNotification(context: Context) {
        appNotificationRepository.showInboxesNotification(
            id = 1,
            title = "5 new mails from Taufik",
            message = "Check them out",
            inboxesItem = listOf(
                ItemInboxNotificationModel("Hey"),
                ItemInboxNotificationModel("How are u?"),
                ItemInboxNotificationModel("Can you check my last email?"),
            )
        )
    }

    override fun showConversationNotification(context: Context) {
        val person1 = ItemPerson(
            id = "first_person",
            name = "First Person",
            image = "https://raw.githubusercontent.com/TutorialsBuzz/cdn/main/android.jpg"
        )
        val person2 = ItemPerson(
            id = "second_person",
            name = "Second Person",
            image = "https://raw.githubusercontent.com/TutorialsBuzz/cdn/main/android.jpg"
        )
        appNotificationRepository.showConversationNotification(
            context, id = 2,
            conversationTitle = "Conversation Title",
            from = person1,
            conversations = listOf(
                ItemConversationNotificationModel(
                    message = "Message From Person 1",
                    timestamp = System.currentTimeMillis(),
                    person = person1
                ),
                ItemConversationNotificationModel(
                    message = "Message From Person 2",
                    timestamp = System.currentTimeMillis(),
                    person = person2
                )
            )
        )
    }

}
