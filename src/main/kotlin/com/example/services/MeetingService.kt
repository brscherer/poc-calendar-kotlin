package com.example.services

import com.example.models.Meeting
import com.example.models.userStorage
import java.time.LocalDateTime

class MeetingService {
    companion object {
        fun checkMeetingOverlap(meeting: Meeting): Meeting? {
            val users = userStorage.filter { it.id in meeting.attendeesIds }

            var overlap: Meeting? = null
            for (user in users) {
                overlap = user.scheduledMeetings.find {
                    !it.startTime.isAfter(meeting.endTime) && !meeting.startTime.isAfter(it.endTime)
                }
                if (overlap != null) {
                    break
                }
            }
            return overlap
        }

        fun findAvailableTimes(schedules: List<Meeting>): List<Pair<LocalDateTime, LocalDateTime>> {
            val sortedMeetings = schedules.sortedBy { it.startTime }

            val availableTimes = mutableListOf<Pair<LocalDateTime, LocalDateTime>>()

            if (sortedMeetings.isEmpty()) {
                val now = LocalDateTime.now()
                availableTimes.add(now to now.plusDays(1))
                return availableTimes
            }

            var currentEndTime = sortedMeetings.first().startTime

            for (meeting in sortedMeetings) {
                if (meeting.startTime.isAfter(currentEndTime)) {
                    availableTimes.add(currentEndTime to meeting.startTime)
                }

                currentEndTime = maxOf(currentEndTime, meeting.endTime, LocalDateTime.now())
            }

            if (currentEndTime.isBefore(LocalDateTime.now().plusDays(1))) {
                availableTimes.add(currentEndTime to LocalDateTime.now().plusDays(1))
            }

            return availableTimes
        }
    }
}