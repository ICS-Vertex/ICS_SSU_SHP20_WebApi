package shp.ssu.icsvertex.nl

import com.example.plugins.configureDatabases
import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import nl.icsvertex.ssu.shp.core.domain.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

import shp.ssu.icsvertex.nl.plugins.*
import java.time.LocalDateTime

fun main() {
    embeddedServer(Netty, port = System.getenv("PORT").toString().trim().toInt(), host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}


fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureHTTP()
    configureSecurity()
    configureRouting()
    install(SwaggerUI) {
        swagger {
            swaggerUrl = "${System.getenv("APPPATH")}/dickSwagger"
        }
        info {
        }
    }

    transaction {
        SchemaUtils.createMissingTablesAndColumns(Article, ArticleBarcode, InventoryOrder, InventoryOrderLine, InventoryOrderLineBarcode, InventoryOrderSetting)
    }
}
