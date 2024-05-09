package com.github.fadlurahmanfdev.core_notification_example.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.github.fadlurahmanfdev.core_notification_example.R
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemConversationNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemInboxNotificationModel
import com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model.ItemPerson
import com.github.fadlurahmanfdev.kotlin_core_notification.data.repositories.NotificationRepository
import com.github.fadlurahmanfdev.kotlin_core_notification.data.repositories.NotificationRepositoryImpl

class AppNotificationRepositoryImpl(
    private val notificationRepository: NotificationRepository,
) : AppNotificationRepository {
    companion object {
        @DrawableRes
        val BANK_MAS_LOGO_ICON = R.drawable.il_logo_bankmas
        const val GENERAL_CHANNEL_ID = NotificationRepositoryImpl.GENERAL_CHANNEL_ID
        const val GENERAL_CHANNEL_NAME = NotificationRepositoryImpl.GENERAL_CHANNEL_NAME
        const val GENERAL_CHANNEL_DESCRIPTION =
            NotificationRepositoryImpl.GENERAL_CHANNEL_DESCRIPTION
        const val CONVERSATION_CHANNEL_ID = "CONVERSATION"
        const val CONVERSATION_CHANNEL_NAME = "Percakapan"
        const val CONVERSATION_CHANNEL_DESCRIPTION =
            "Notifikasi Percakapan"
        const val VOIP_CHANNEL_ID = "VOIP"
        const val VOIP_CHANNEL_NAME = "Panggilan"
        const val VOIP_CHANNEL_DESCRIPTION =
            "Notifikasi Panggilan"
        const val CUSTOM_CHANNEL_ID = "CUSTOM"
        const val CUSTOM_CHANNEL_NAME = "Custom"
        const val CUSTOM_CHANNEL_DESCRIPTION =
            "Notifikasi Custom"
    }

    private fun createGeneralChannel(context: Context) {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        notificationRepository.createNotificationChannel(
            context,
            channelId = GENERAL_CHANNEL_ID,
            channelName = GENERAL_CHANNEL_NAME,
            channelDescription = GENERAL_CHANNEL_DESCRIPTION,
            sound = sound,
        )
    }

    private fun createConversationChannel(context: Context) {
        val sound =
            Uri.parse("android.resource://" + context.packageName + "/" + R.raw.messaging_notification_sound)
        notificationRepository.createNotificationChannel(
            context,
            channelId = CONVERSATION_CHANNEL_ID,
            channelName = CONVERSATION_CHANNEL_NAME,
            channelDescription = CONVERSATION_CHANNEL_DESCRIPTION,
            sound = sound,
        )
    }

    override fun createVOIPChannel(context: Context) {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        notificationRepository.createNotificationChannel(
            context,
            channelId = VOIP_CHANNEL_ID,
            channelName = VOIP_CHANNEL_NAME,
            channelDescription = VOIP_CHANNEL_DESCRIPTION,
            sound = sound,
        )
    }

    private fun createCustomChannel(context: Context) {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        notificationRepository.createNotificationChannel(
            context,
            channelId = CUSTOM_CHANNEL_ID,
            channelName = CUSTOM_CHANNEL_NAME,
            channelDescription = CUSTOM_CHANNEL_DESCRIPTION,
            sound = sound,
        )
    }

    override fun askPermission(
        context: Context,
        onCompleteCheckPermission: (isGranted: Boolean) -> Unit
    ) {
        notificationRepository.askNotificationPermission(context, onCompleteCheckPermission)
    }

    override fun showNotification(context: Context, id: Int, title: String, message: String) {
        askPermission(context, onCompleteCheckPermission = { isGranted ->
            if (isGranted) {
                createGeneralChannel(context)
                notificationRepository.showBasicNotification(
                    context,
                    id = id,
                    title = title,
                    message = message,
                    smallIcon = BANK_MAS_LOGO_ICON,
                    groupKey = null,
                    pendingIntent = null
                )
            } else {
                Log.d(
                    AppNotificationRepositoryImpl::class.java.simpleName,
                    "unable to showNotification cause permission is not granted"
                )
            }
        })
    }

    override fun showImageNotification(
        context: Context,
        id: Int,
        title: String,
        message: String,
        imageUrl: String,
    ) {
        askPermission(context, onCompleteCheckPermission = { isGranted ->
            if (isGranted) {
                createGeneralChannel(context)
                notificationRepository.showBasicImageNotification(
                    context,
                    id = id,
                    title = title,
                    message = message,
                    smallIcon = BANK_MAS_LOGO_ICON,
                    imageUrl = imageUrl,
                    pendingIntent = null,
                )
            } else {
                Log.d(
                    AppNotificationRepositoryImpl::class.java.simpleName,
                    "unable to showImageNotification cause permission is not granted"
                )
            }
        })
    }

    override fun showCustomImageNotification(
        context: Context,
        id: Int,
        title: String,
        message: String,
        imageUrl: String
    ) {
        askPermission(context, onCompleteCheckPermission = { isGranted ->
            if (isGranted) {
                createCustomChannel(context)
                val notificationBuilder = NotificationCompat.Builder(context, CUSTOM_CHANNEL_ID)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setSmallIcon(BANK_MAS_LOGO_ICON)
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
                            notificationRepository.showCustomNotification(
                                context,
                                id = id,
                                notificationBuilder = notificationBuilder
                            )
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            super.onLoadFailed(errorDrawable)
                            notificationRepository.showCustomNotification(
                                context,
                                id = id,
                                notificationBuilder = notificationBuilder
                            )
                        }

                    })
            } else {
                Log.d(
                    AppNotificationRepositoryImpl::class.java.simpleName,
                    "unable to showCustomImageNotification cause permission is not granted"
                )
            }
        })
    }

    override fun showInboxesNotification(
        context: Context,
        id: Int,
        title: String,
        message: String,
        inboxesItem: List<ItemInboxNotificationModel>
    ) {
        notificationRepository.showBasicInboxNotification(
            context,
            id = id,
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
        askPermission(context, onCompleteCheckPermission = { isGranted ->
            if (isGranted) {
                createConversationChannel(context)
                notificationRepository.showBasicConversationNotification(
                    context,
                    id = id,
                    smallIcon = BANK_MAS_LOGO_ICON,
                    conversationTitle = conversationTitle,
                    conversationFrom = from,
                    conversations = conversations
                )
            } else {
                Log.d(
                    AppNotificationRepositoryImpl::class.java.simpleName,
                    "unable to showConversationNotification cause permission is not granted"
                )
            }
        })
    }
}