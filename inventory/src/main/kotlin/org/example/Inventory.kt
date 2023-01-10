package org.example

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.*

const val servicePort = 8080
const val sidecarPort = 3500
const val pubSubName = "item-pub-sub"
const val topic = "items"

fun main() {
    embeddedServer(Netty, port = servicePort, host = "0.0.0.0", module = Application::module).start(wait = true)
}

@Serializable
data class ItemCloudEvent(
    val id: String,
    val source: String,
    val specversion: String,
    val type: String,
    val datacontenttype: String,
    val `data`: Item,
)

@Serializable
data class Item (
    val id: String = UUID.randomUUID().toString(),
    val name: String
)

fun Application.module() {
    this@module.routing {

        post("/{item}") {
            val client = HttpClient(CIO) {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                    })
                }
            }

            val event = ItemCloudEvent(
                id = UUID.randomUUID().toString(),
                source = "inventory",
                specversion = "1.0",
                type = "org.example.item",
                datacontenttype = ContentType.Application.Json.toString(),
                data = Item(name = call.parameters["item"].toString())
            )

            client.post("http://localhost:${sidecarPort}/v1.0/publish/$pubSubName/$topic") {
                contentType(ContentType.parse("application/cloudevents+json"))
                setBody(event)
            }.also {
                val msg = "Published $event (${it.status})"
                println(msg)
                return@post call.respondText(msg)
            }
        }
    }
}