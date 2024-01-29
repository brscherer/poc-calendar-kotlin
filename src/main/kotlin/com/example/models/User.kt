package com.example.models

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class User(val id: Int, val name: String, var scheduledMeetings: List<Meeting>)

val userStorage = mutableListOf<User>(
    User(1, "Bruno", listOf(
        Meeting(1, listOf(1), LocalDateTime.now(), LocalDateTime.now().plusHours(1)),
        Meeting(2, listOf(1), LocalDateTime.now().plusHours(5), LocalDateTime.now().plusHours(6))
    )),
    User(2, "Raphael", listOf(
        Meeting(3, listOf(2), LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(5)),
        Meeting(4, listOf(2), LocalDateTime.now().plusHours(7), LocalDateTime.now().plusHours(8))
    ))
)
