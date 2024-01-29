package com.example.models

import com.example.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Meeting(
    var id: Int? = null,
    val attendeesIds: List<Int>,
    @Serializable(with = LocalDateTimeSerializer::class) val startTime: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class) val endTime: LocalDateTime
)

val meetingStorage = mutableListOf<Meeting>(
    Meeting(1, listOf(1), LocalDateTime.now(), LocalDateTime.now().plusHours(1)),
    Meeting(2, listOf(1), LocalDateTime.now().plusHours(5), LocalDateTime.now().plusHours(6)),
    Meeting(3, listOf(2), LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(5)),
    Meeting(4, listOf(2), LocalDateTime.now().plusHours(7), LocalDateTime.now().plusHours(8))
)

var meetingIdCounter = 4
