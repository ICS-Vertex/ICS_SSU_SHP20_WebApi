package shp.ssu.icsvertex.nl.plugins

import com.google.gson.Gson
import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.github.smiley4.ktorswaggerui.dsl.post
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.resources.Resources
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.*
import nl.icsvertex.ssu.shp.core.models.inventory.InventoryOrder
import shp.ssu.icsvertex.nl.inventory.processInput
import java.io.File
import java.util.zip.ZipEntry

import java.util.zip.ZipInputStream

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
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
                multiPartData.forEachPart { file ->
                    when (file) {
                        is PartData.FileItem -> {
                            if (file.originalFileName!!.lowercase().contains(".zip")) {

                                val zipFile = ZipInputStream(file.streamProvider())

                                val inventoryOrder = buildList<InventoryOrder> {
                                    var zipEntry: ZipEntry? = zipFile.nextEntry
                                    while (zipEntry != null){
                                        if(!zipEntry.isDirectory){
                                            zipFile.bufferedReader(charset("UTF-8")).readText().let {
                                                add(Gson().fromJson(it, InventoryOrder::class.java))
                                            }
                                        }
                                        zipEntry = zipFile.nextEntry
                                    }
                                }

                                inventoryOrder.forEach { it.processInput() }

                            }
                        }

                        else -> throw BadRequestException("Import failed")
                    }
                }
            }
        }
//    }
}