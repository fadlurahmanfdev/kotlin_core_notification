package com.github.fadlurahmanfdev.core_notification.data.dto.model

data class ItemMessagingNotificationModel(
    val message: String,
    val messageFrom: String,
    val personImageFromNetwork: String? = null,
    val timestamp: Long,
)
