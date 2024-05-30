package com.github.fadlurahmanfdev.kotlin_core_notification.data.repositories

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
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
import com.github.fadlurahmanfdev.kotlin_core_notification.others.BaseNotificationService

class NotificationRepositoryImpl(context: Context) : BaseNotificationService(context),
    NotificationRepository {

    companion object {
        const val GENERAL_CHANNEL_ID = "GENERAL"
        const val GENERAL_CHANNEL_NAME = "Umum"
        const val GENERAL_CHANNEL_DESCRIPTION = "Notifikasi Umum"
    }

    override fun isNotificationPermissionEnabledAndGranted(): Boolean {
        return super.isNotificationPermissionEnabledAndGranted()
    }

    override fun isNotificationChannelExist(channelId: String): Boolean {
        return super.isNotificationChannelExist(channelId)
    }

    private fun isCanShowGeneralNotification(): Boolean {
        if (!isNotificationPermissionEnabledAndGranted()) {
            Log.i(
                NotificationRepositoryImpl::class.java.simpleName,
                "permission notification is not granted"
            )
            return false
        }
        if (!isSupportedNotificationChannel()) return false
        if (isNotificationChannelExist(GENERAL_CHANNEL_ID)) return true
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        return createNotificationChannel(
            GENERAL_CHANNEL_ID,
            GENERAL_CHANNEL_NAME,
            GENERAL_CHANNEL_DESCRIPTION,
            sound,
        )
    }

    override fun createNotificationChannel(
        channelId: String,
        channelName: String,
        channelDescription: String,
        sound: Uri,
    ): Boolean {
        return super.createNotificationChannel(
            channelId = channelId,
            channelName = channelName,
            channelDescription = channelDescription,
            sound = sound
        )
    }

    override fun deleteNotificationChannel(channelId: String): Boolean {
        return super.deleteNotificationChannel(channelId)
    }

    override fun showNotification(
        id: Int,
        title: String,
        message: String,
        @DrawableRes smallIcon: Int,
        groupKey: String?,
        pendingIntent: PendingIntent?,
    ) {
        if (isCanShowGeneralNotification()) {
            val notification =
                notificationBuilder(GENERAL_CHANNEL_ID, smallIcon).apply {
                    setContentTitle(title)
                    setContentText(message)
                    if (groupKey != null) {
                        setGroup(groupKey)
                    }
                    if (pendingIntent != null) {
                        setContentIntent(pendingIntent)
                    }
                }
            notificationManager.notify(id, notification.build())
        }
    }

    override fun showCustomNotification(
        channelId: String,
        id: Int,
        title: String,
        message: String,
        @DrawableRes smallIcon: Int,
        groupKey: String?,
        pendingIntent: PendingIntent?,
    ) {
        val notification =
            notificationBuilder(channelId, smallIcon).apply {
                setContentTitle(title)
                setContentText(message)
                if (groupKey != null) {
                    setGroup(groupKey)
                }
                if (pendingIntent != null) {
                    setContentIntent(pendingIntent)
                }
            }
        notificationManager.notify(id, notification.build())
    }

    override fun showCustomNotification(
        id: Int,
        notificationBuilder: NotificationCompat.Builder,
    ) {
        notificationManager.notify(id, notificationBuilder.build())
    }

    override fun showImageNotification(
        id: Int,
        title: String,
        message: String,
        imageUrl: String,
        @DrawableRes smallIcon: Int,
        pendingIntent: PendingIntent?,
    ) {
        if (isCanShowGeneralNotification()) {
            val notificationBuilder =
                notificationBuilder(GENERAL_CHANNEL_ID, smallIcon).apply {
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
                        notificationBuilder.setLargeIcon(resource)
                        notificationBuilder.setStyle(
                            NotificationCompat.BigPictureStyle().bigPicture(resource)
                                .bigLargeIcon(resource)
                        )
                        notificationManager.notify(id, notificationBuilder.build())
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        Log.e(
                            NotificationRepositoryImpl::class.java.simpleName,
                            "failed onLoadFailed"
                        )
                        notificationManager.notify(id, notificationBuilder.build())
                    }

                })
        }
    }

    override fun showImageNotification(
        id: Int,
        title: String,
        message: String,
        image: Bitmap,
        smallIcon: Int,
        pendingIntent: PendingIntent?
    ) {
        if (isCanShowGeneralNotification()) {
            val notificationBuilder =
                notificationBuilder(GENERAL_CHANNEL_ID, smallIcon).apply {
                    setContentTitle(title)
                    setContentText(message)

                    if (pendingIntent != null) {
                        setContentIntent(pendingIntent)
                    }
                }
            notificationBuilder.setLargeIcon(image)
            notificationBuilder.setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(image)
                    .bigLargeIcon(image)
            )
            notificationManager.notify(id, notificationBuilder.build())
        }
    }

    override fun showImageNotification(
        channelId: String,
        id: Int,
        title: String,
        message: String,
        imageUrl: String,
        @DrawableRes smallIcon: Int,
        pendingIntent: PendingIntent?,
    ) {
        if (isCanShowGeneralNotification()) {
            val notificationBuilder =
                notificationBuilder(channelId, smallIcon).apply {
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
                        notificationBuilder.setLargeIcon(resource)
                        notificationBuilder.setStyle(
                            NotificationCompat.BigPictureStyle().bigPicture(resource)
                                .bigLargeIcon(resource)
                        )
                        notificationManager.notify(id, notificationBuilder.build())
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        Log.e(
                            NotificationRepositoryImpl::class.java.simpleName,
                            "failed onLoadFailed"
                        )
                        notificationManager.notify(id, notificationBuilder.build())
                    }

                })
        }
    }

    override fun showImageNotification(
        channelId: String,
        id: Int,
        title: String,
        message: String,
        image: Bitmap,
        smallIcon: Int,
        pendingIntent: PendingIntent?
    ) {
        if (isCanShowGeneralNotification()) {
            val notificationBuilder =
                notificationBuilder(channelId, smallIcon).apply {
                    setContentTitle(title)
                    setContentText(message)

                    if (pendingIntent != null) {
                        setContentIntent(pendingIntent)
                    }
                }
            notificationBuilder.setLargeIcon(image)
            notificationBuilder.setStyle(
                NotificationCompat.BigPictureStyle().bigPicture(image)
                    .bigLargeIcon(image)
            )
            notificationManager.notify(id, notificationBuilder.build())
        }
    }

    override fun showInboxNotification(
        id: Int,
        title: String,
        text: String,
        @DrawableRes smallIcon: Int,
        inboxes: List<ItemInboxNotificationModel>
    ) {
        if (isCanShowGeneralNotification()) {
            showCustomInboxNotification(
                id = id,
                channelId = GENERAL_CHANNEL_ID,
                title = title,
                text = text,
                smallIcon = smallIcon,
                inboxes = inboxes
            )
        }
    }

    override fun showCustomInboxNotification(
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
            notificationBuilder(channelId, smallIcon).apply {
                setContentTitle(title)
                setContentText(text)
                setStyle(inboxStyle)
            }
        notificationManager.notify(id, notificationBuilder.build())
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

    override fun showConversationNotification(
        id: Int,
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
            id = id,
            channelId = GENERAL_CHANNEL_ID,
            smallIcon = smallIcon,
            conversationTitle = conversationTitle,
            conversationFrom = conversationFrom,
            conversations = conversations,
        )
    }

    override fun showCustomConversationNotification(
        id: Int,
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
                                    notificationManager.notify(
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
            notify(id, builder.build())
        }
    }

    override fun cancelNotification(context: Context, id: Int) {
        notificationManager.cancel(id)
    }
}