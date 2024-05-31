package com.github.fadlurahmanfdev.kotlin_core_notification.others

import android.app.Notification
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemConversationNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemGroupedNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemInboxNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemPerson

abstract class BaseMessagingNotificationService(context: Context) :
    BaseNotificationService(context) {
    open fun showInboxNotification(
        notificationId: Int,
        title: String,
        text: String,
        @DrawableRes smallIcon: Int,
        inboxes: List<ItemInboxNotificationModel>
    ) {
        if (isCanShowGeneralNotification()) {
            showCustomInboxNotification(
                notificationId = notificationId,
                channelId = GENERAL_CHANNEL_ID,
                title = title,
                text = text,
                smallIcon = smallIcon,
                inboxes = inboxes
            )
        }
    }

    open fun showCustomInboxNotification(
        notificationId: Int,
        channelId: String,
        title: String,
        text: String,
        @DrawableRes smallIcon: Int,
        inboxes: List<ItemInboxNotificationModel>
    ) {
        val inboxStyle = NotificationCompat.InboxStyle()
        repeat(inboxes.size) { index ->
            inboxStyle.addLine(inboxes[index].line)
        }
        val notificationBuilder =
            notificationBuilder(channelId, smallIcon).apply {
                setContentTitle(title)
                setContentText(text)
                setStyle(inboxStyle)
            }
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun getPersonMessagingStyle(
        context: Context,
        item: ItemPerson,
        onComplete: (String, Person.Builder) -> Unit
    ) {
        val personBuilder = Person.Builder().setName(item.name)
        if (item.image != null) {
            Glide.with(context)
                .asBitmap()
                .load(item.image)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        personBuilder.setIcon(IconCompat.createWithBitmap(resource))
                        onComplete(item.id, personBuilder)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        onComplete(item.id, personBuilder)
                    }

                })
        } else {
            onComplete(item.id, personBuilder)
        }
    }

    open fun showConversationNotification(
        notificationId: Int,
        @DrawableRes smallIcon: Int,
        conversationTitle: String,
        conversationFrom: ItemPerson,
        conversations: List<ItemConversationNotificationModel>,
    ) {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        createNotificationChannel(
            channelId = GENERAL_CHANNEL_ID,
            channelName = GENERAL_CHANNEL_NAME,
            channelDescription = GENERAL_CHANNEL_DESCRIPTION,
            sound = sound
        )
        showCustomConversationNotification(
            notificationId = notificationId,
            channelId = GENERAL_CHANNEL_ID,
            smallIcon = smallIcon,
            conversationTitle = conversationTitle,
            conversationFrom = conversationFrom,
            conversations = conversations,
        )
    }

    open fun showCustomConversationNotification(
        notificationId: Int,
        channelId: String,
        @DrawableRes smallIcon: Int,
        conversationTitle: String,
        conversationFrom: ItemPerson,
        conversations: List<ItemConversationNotificationModel>
    ) {
        val notificationBuilder = notificationBuilder(
            channelId = channelId,
            smallIcon = smallIcon
        )
        val mapPerson = hashMapOf<String, Person.Builder>()
        var counter = 0
        getPersonMessagingStyle(
            context,
            item = conversationFrom,
            onComplete = { personId, personBuilder ->
                mapPerson[personId] = personBuilder
                val messagingStyle = NotificationCompat.MessagingStyle(personBuilder.build())
                    .setConversationTitle(conversationTitle)
                repeat(conversations.size) { index ->
                    val conversation = conversations[index]
                    if (mapPerson.containsKey(conversation.person.id)) {
                        val person = mapPerson[conversation.person.id]!!
                        messagingStyle.addMessage(
                            NotificationCompat.MessagingStyle.Message(
                                conversation.message,
                                conversation.timestamp,
                                person.build(),
                            )
                        )
                        counter++
                        if (counter >= conversations.size) {
                            notificationBuilder.setStyle(messagingStyle)
                            notificationManager.notify(
                                notificationId,
                                notificationBuilder.build()
                            )
                        }
                    } else {
                        getPersonMessagingStyle(
                            context,
                            item = conversation.person,
                            onComplete = { personIdElement, personBuilderElement ->
                                messagingStyle.addMessage(
                                    NotificationCompat.MessagingStyle.Message(
                                        conversation.message,
                                        conversation.timestamp,
                                        personBuilderElement.build(),
                                    )
                                )
                                mapPerson[personIdElement] = personBuilderElement
                                counter++
                                if (counter >= conversations.size) {
                                    notificationBuilder.setStyle(messagingStyle)
                                    notificationManager.notify(
                                        notificationId,
                                        notificationBuilder.build()
                                    )
                                }
                            })
                    }
                }
            },
        )
    }

    private fun showGroupedNotification(
        notificationId: Int,
        channelId: String,
        groupKey: String,
        bigContentTitle: String,
        summaryText: String,
        @DrawableRes smallIcon: Int,
        itemLine: List<String>,
        itemNotifications: List<ItemGroupedNotificationModel>,
    ) {
        val notifications: ArrayList<Notification> = arrayListOf()
        for (index in itemNotifications.indices) {
            notifications.add(
                notificationBuilder(
                    channelId = channelId,
                    smallIcon = smallIcon
                ).apply {
                    setContentTitle(itemNotifications[index].title)
                    setContentText(itemNotifications[index].message)
                    setGroup(groupKey)
                }.build()
            )
        }
        val builder = groupNotificationBuilder(
            context,
            channelId = channelId,
            groupKey = groupKey,
            bigContentTitle = bigContentTitle,
            summaryText = summaryText,
            lines = itemLine,
            smallIcon = smallIcon,
        )
        notificationManager.apply {
            for (index in itemNotifications.indices) {
                notify(itemNotifications[index].id, notifications[index])
            }
            notify(notificationId, builder.build())
        }
    }
}