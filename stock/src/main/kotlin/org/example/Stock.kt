package org.example

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val servicePort = 8081

fun main() {
    embeddedServer(Netty, port = servicePort, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    this@module.routing {

        post("/pen") {
            println("Received a pen:\n${call.receiveText()}")
            call.respond(HttpStatusCode.OK)
        }

        post("/book") {
            println("Received a book:\n${call.receiveText()}")
            call.respond(HttpStatusCode.OK)
        }

        post("/cup") {
            println("Received a cup:\n${call.receiveText()}")
            call.respond(HttpStatusCode.OK)
        }

        post("/fork") {
            println("Received a fork:\n${call.receiveText()}")
            call.respond(HttpStatusCode.OK)
        }

        post("/spoon") {
            println("Received a spoon:\n${call.receiveText()}")
            call.respond(HttpStatusCode.OK)
        }

        post("/") {
            println("Received an undefined item:\n${call.receiveText()}")
            call.respond(HttpStatusCode.OK)
        }
    }
}