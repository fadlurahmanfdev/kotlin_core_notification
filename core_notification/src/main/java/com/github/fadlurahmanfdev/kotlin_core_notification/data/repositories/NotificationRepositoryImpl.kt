package com.github.fadlurahmanfdev.kotlin_core_notification.data.repositories

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemConversationNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemGroupedNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemInboxNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemPerson
import com.github.fadlurahmanfdev.kotlin_core_notification.others.BaseNotificationService

class NotificationRepositoryImpl : BaseNotificationService(),
    NotificationRepository {

    companion object {
        const val GENERAL_CHANNEL_ID = "GENERAL"
        const val GENERAL_CHANNEL_NAME = "Umum"
        const val GENERAL_CHANNEL_DESCRIPTION = "Notifikasi Umum"
    }

    override fun askNotificationPermission(
        context: Context,
        onCompleteCheckPermission: (isGranted: Boolean) -> Unit
    ) {
        return baseAskNotificationPermission(
            context,
            onCompleteCheckPermission = onCompleteCheckPermission
        )
    }

    override fun isNotificationPermissionGranted(context: Context): Boolean {
        return baseIsNotificationPermissionGranted(context)
    }

    override fun isNotificationChannelExist(context: Context, channelId: String): Boolean {
        return baseIsNotificationChannelExist(context, channelId = channelId)
    }

    override fun createNotificationChannel(
        context: Context,
        channelId: String,
        channelName: String,
        channelDescription: String,
        sound: Uri,
    ) {
        return baseCreateNotificationChannel(
            context,
            channelId = channelId,
            channelName = channelName,
            channelDescription = channelDescription,
            sound = sound
        )
    }

    override fun deleteNotificationChannel(context: Context, channelId: String) {
        return baseDeleteNotificationChannel(context, channelId = channelId)
    }

    override fun showBasicNotification(
        context: Context,
        id: Int,
        title: String,
        message: String,
        @DrawableRes smallIcon: Int,
        groupKey: String?,
        pendingIntent: PendingIntent?,
    ) {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        createNotificationChannel(
            context,
            GENERAL_CHANNEL_ID,
            GENERAL_CHANNEL_NAME,
            GENERAL_CHANNEL_DESCRIPTION,
            sound,
        )
        val notification =
            notificationBuilder(context, GENERAL_CHANNEL_ID, smallIcon).apply {
                setContentTitle(title)
                setContentText(message)
                if (groupKey != null) {
                    setGroup(groupKey)
                }
                if (pendingIntent != null) {
                    setContentIntent(pendingIntent)
                }
            }
        getNotificationManager(context).notify(id, notification.build())
    }

    override fun showBasicImageNotification(
        context: Context,
        id: Int,
        title: String,
        message: String,
        imageUrl: String,
        @DrawableRes smallIcon: Int,
        pendingIntent: PendingIntent?,
    ) {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        createNotificationChannel(
            context,
            GENERAL_CHANNEL_ID,
            GENERAL_CHANNEL_NAME,
            GENERAL_CHANNEL_DESCRIPTION,
            sound,
        )
        val notificationBuilder =
            notificationBuilder(context, GENERAL_CHANNEL_ID, smallIcon).apply {
                setContentTitle(title)
                setContentText(message)

                if (pendingIntent != null) {
                    setContentIntent(pendingIntent)
                }
            }
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    val bitmap: Bitmap? = null
                    notificationBuilder.setLargeIcon(resource)
                    notificationBuilder.setStyle(
                        NotificationCompat.BigPictureStyle().bigPicture(resource)
                            .bigLargeIcon(bitmap)
                    )
                    getNotificationManager(context)
                        .notify(id, notificationBuilder.build())
                }

                override fun onLoadCleared(placeholder: Drawable?) {}

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    Log.e(NotificationRepositoryImpl::class.java.simpleName, "failed onLoadFailed")
                    getNotificationManager(context)
                        .notify(id, notificationBuilder.build())
                }

            })
    }

    override fun showBasicInboxNotification(
        context: Context,
        id: Int,
        title: String,
        text: String,
        @DrawableRes smallIcon: Int,
        inboxes: List<ItemInboxNotificationModel>
    ) {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        createNotificationChannel(
            context,
            GENERAL_CHANNEL_ID,
            GENERAL_CHANNEL_NAME,
            GENERAL_CHANNEL_DESCRIPTION,
            sound,
        )
        showCustomInboxNotification(
            context,
            id = id,
            channelId = GENERAL_CHANNEL_ID,
            title = title,
            text = text,
            smallIcon = smallIcon,
            inboxes = inboxes
        )
    }

    override fun showCustomInboxNotification(
        context: Context,
        id: Int,
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
            notificationBuilder(context, channelId, smallIcon).apply {
                setContentTitle(title)
                setContentText(text)
                setStyle(inboxStyle)
            }
        getNotificationManager(context).notify(id, notificationBuilder.build())
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

    override fun showBasicConversationNotification(
        context: Context,
        id: Int,
        @DrawableRes smallIcon: Int,
        conversationTitle: String,
        conversationFrom: ItemPerson,
        conversations: List<ItemConversationNotificationModel>,
    ) {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        createNotificationChannel(
            context,
            channelId = GENERAL_CHANNEL_ID,
            channelName = GENERAL_CHANNEL_NAME,
            channelDescription = GENERAL_CHANNEL_DESCRIPTION,
            sound = sound
        )
        showCustomConversationNotification(
            context,
            id = id,
            channelId = GENERAL_CHANNEL_ID,
            smallIcon = smallIcon,
            conversationTitle = conversationTitle,
            conversationFrom = conversationFrom,
            conversations = conversations,
        )
    }

    override fun showCustomConversationNotification(
        context: Context,
        id: Int,
        channelId: String,
        @DrawableRes smallIcon: Int,
        conversationTitle: String,
        conversationFrom: ItemPerson,
        conversations: List<ItemConversationNotificationModel>
    ) {
        val notificationBuilder = notificationBuilder(
            context,
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
                            getNotificationManager(context).notify(
                                id,
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
                                    getNotificationManager(context).notify(
                                        id,
                                        notificationBuilder.build()
                                    )
                                }
                            })
                    }
                }
            })
    }

    @Deprecated("not ready yet")
    override fun showGroupedNotification(
        context: Context,
        id: Int,
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
                    context,
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
        getNotificationManager(context).apply {
            for (index in itemNotifications.indices) {
                notify(itemNotifications[index].id, notifications[index])
            }
            notify(id, builder.build())
        }
    }

    override fun showCustomNotification(
        context: Context,
        id: Int,
        notificationBuilder: NotificationCompat.Builder,
    ) {
        getNotificationManager(context).notify(id, notificationBuilder.build())
    }

    override fun cancelNotification(context: Context, id: Int) {
        getNotificationManager(context).cancel(id)
    }
}