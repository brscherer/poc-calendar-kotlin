package com.example.routes

import com.example.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRouting() {
    route("/users") {
        get {
            if (userStorage.isNotEmpty()) {
                call.respond(userStorage)
            } else {
                call.respondText("No users found", status = HttpStatusCode.OK)
            }
        }
    }
}