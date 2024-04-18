package shp.ssu.icsvertex.nl

import com.example.plugins.configureDatabases
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*

import shp.ssu.icsvertex.nl.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}


fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureHTTP()
    configureSecurity()
    configureRouting()

i
    transaction {
        SchemaUtils.createMissingTablesAndColumns(IsKutTestTable)

        IsKutTestTableEntity.new {
            henk = "hello"
            herman = "henk"
        }
    }

    transaction{
        IsKutTestTableEntity.find {
            IsKutTestTable.henk eq "pandabeer"
            IsKutTestTable.herman eq "Hans"
        }
    }
}