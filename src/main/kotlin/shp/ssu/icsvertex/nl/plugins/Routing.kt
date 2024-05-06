package shp.ssu.icsvertex.nl.plugins

import io.github.smiley4.ktorswaggerui.dsl.post
import io.github.smiley4.ktorswaggerui.dsl.get
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.icsvertex.ssu.shp.core.dto.ArticleDto
import nl.icsvertex.ssu.shp.core.dto.InventoryCompleteOrderDto
import nl.icsvertex.ssu.shp.core.dto.InventoryOrderDto
import nl.icsvertex.ssu.shp.core.dto.InventoryOrderStatusDto
import shp.ssu.icsvertex.nl.exceptions.InventoryOrderInsertException
import shp.ssu.icsvertex.nl.exceptions.InventoryOrderNotFoundException
import shp.ssu.icsvertex.nl.inventory.*
import java.io.File

fun Application.configureRouting() {
    install(StatusPages) {
        exception<InventoryOrderInsertException> {call, cause ->
            call.respond (HttpStatusCode.InternalServerError, cause.msg)
        }
        exception <InventoryOrderNotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, cause.msg)
        }

        //Always last resort
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }

    }
    install(Resources)
    routing {
        route(System.getenv("APPPATH")) {
            post("importInventory", {
                tags = listOf("Import")
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
                    HttpStatusCode.OK to {body<List<String>>()}
                }
            }) {
                val multiPartData = call.receiveMultipart()
                call.respond (HttpStatusCode.OK,  processFiles(multiPartData))
            }

            get("getArticlesForCustomer", {
                tags = listOf("Article")
                request {
                    pathParameter<String>("customerNo")
                }
                response{
                    HttpStatusCode.OK to {body<List<ArticleDto>>()}
                }
            }){
                val input = call.parameters["customerNo"]
                if (input != null){
                    call.respond(getArticles(input))
                }
            }
            get("getInventoryOrderStatuses", {
                tags = listOf("Status")
                request {
                }
                response{
                    HttpStatusCode.OK to {body<List<InventoryOrderStatusDto>>()}
                }
            }){
                call.respond(getStatuses())
            }
            get("getInventoryOrders/{customerNo}", {
                tags = listOf("Order")
                request {
                    pathParameter<String>("customerNo")
                }
                response{
                    HttpStatusCode.OK to {body<List<InventoryOrderDto>>()}
                }
            }){
                val input  = call.parameters["customerNo"]
                if (input != null){
                    call.respond(getOrders(input))
                }

            }
            get("getInventoryOrder/{orderNo}/{customerNo}", {
                tags = listOf("Order")
                request {
                    pathParameter<String>("orderNo")
                    pathParameter<String>("customerNo")
                }
                response{
                    HttpStatusCode.OK to {body<List<InventoryCompleteOrderDto>>()}
                }
            }){
                val orderNo = call.parameters["orderNo"]
                val customerNo = call.parameters["customerNo"]
                if (orderNo != null && customerNo != null){
                    call.respond(getOrder(orderNo, customerNo))
                }
            }
        }
    }
}