package org.example

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val servicePort = 8082

fun main() {
    embeddedServer(Netty, port = servicePort, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    routing {

        post("/office") {
            println("Received an office item:\n${call.receiveText()}")
            call.respond(HttpStatusCode.OK)
        }

        post("/kitchen") {
            println("Received a kitchen item:\n${call.receiveText()}")
            call.respond(HttpStatusCode.OK)
        }

        post("/") {
            println("Received an undefined item:\n${call.receiveText()}")
            call.respond(HttpStatusCode.OK)
        }
    }
}