package shp.ssu.icsvertex.nl.plugins

import io.github.smiley4.ktorswaggerui.dsl.post
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import shp.ssu.icsvertex.nl.inventory.processFiles
import java.io.File

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    install(Resources)
    routing {
//        route("\"${System.getenv("APPPATH")}" ) {
        post("importInventory", {
            request {
                multipartBody {
                    mediaType(ContentType.MultiPart.FormData)
                    required = true
                    part<File>("zipfile") {
                        mediaTypes = setOf(
                            ContentType.Application.Zip
                        )
                    }
                }
            }
            response {
                HttpStatusCode.OK
            }
        }) {
            val multiPartData = call.receiveMultipart()
            processFiles(multiPartData)
            call.respondText(text = "200", status = HttpStatusCode.OK)
        }
    }
//    }
}