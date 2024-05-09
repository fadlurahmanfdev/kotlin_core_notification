package com.github.fadlurahmanfdev.kotlin_core_notification.data.dto.model

data class ItemConversationNotificationModel(
    val message: String,
    val timestamp: Long,
    val person: ItemPerson,
)
