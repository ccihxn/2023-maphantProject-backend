package com.tovelop.maphant.dto

import java.time.LocalDateTime

data class RoomListResultDto(
    val id: Int,
    val last_content: String,
    val sender_id: Int,
    val receiver_id: Int,
    val time: LocalDateTime,
    var sender_is_deleted: Boolean,
    var receiver_is_deleted: Boolean,
    val other_id: Int,
    val other_nickname: String,
    val unread_count: Int,
)