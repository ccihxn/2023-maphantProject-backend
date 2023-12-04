package com.tovelop.maphant.dto

import java.time.LocalDateTime

data class CreateRoomDto(
    val last_content: String,
    val time: LocalDateTime,
    val sender_id: Int,
    val receiver_id: Int,
    val sender_is_deleted: Boolean,
    val receiver_is_deleted: Boolean,
    val sender_unread_count: Int,
    val receiver_unread_count: Int,
)
