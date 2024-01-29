package com.example.routes

import com.example.models.*
import com.example.services.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.format.DateTimeFormatter

fun Route.meetingRouting() {
    route("/meetings") {
        get {
            if (meetingStorage.isNotEmpty()) {
                call.respond(meetingStorage)
            } else {
                call.respondText("No meetings found", status = HttpStatusCode.OK)
            }
        }
        get("/user/{id?}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing user id",
                status = HttpStatusCode.BadRequest
            )
            val user =
                userStorage.find { it.id == id.toInt() } ?: return@get call.respondText(
                    "No customer with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(user.scheduledMeetings)
        }
        post("book") {
            val meeting = call.receive<Meeting>()
            val overlap = MeetingService.checkMeetingOverlap(meeting)
            if (overlap != null) {
                call.respondText("Meeting overlap with another meeting that start at ${overlap.startTime} and ends at ${overlap.endTime}", status = HttpStatusCode.Conflict)
            } else {
                meetingIdCounter++
                meeting.id = meetingIdCounter
                meetingStorage.add(meeting)
                call.respondText("Meeting scheduled", status = HttpStatusCode.Created)
            }
        }
        post("best-time") {
            val attendeesIds = call.receive<List<Int>>()
            val users = userStorage.filter { it.id in attendeesIds }
            val scheduledMeetings = users.flatMap { it.scheduledMeetings }
            val availableTimes = MeetingService.findAvailableTimes(scheduledMeetings)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formattedTimes = availableTimes.joinToString(",\n ") { interval ->
            "(" +
                    interval.first.format(formatter) +
                    ", " +
                    interval.second.format(formatter) +
                    ")"
        }

            call.respondText("Best time:\n $formattedTimes", status = HttpStatusCode.Created)
        }
        delete("{id?}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (meetingStorage.removeIf { it.id == id.toInt() }) {
                call.respondText("Meeting canceled", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}