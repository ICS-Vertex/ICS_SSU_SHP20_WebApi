package shp.ssu.icsvertex.nl.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*

fun Application.configureHTTP() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)

        allowHeader(HttpHeaders.AccessControlAllowHeaders)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.Cookie)
        allowHeader(HttpHeaders.XForwardedProto)
        allowHeader(HttpHeaders.Connection)
        allowHeader(HttpHeaders.ContentLength)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.Accept)
        allowHeader(HttpHeaders.AcceptEncoding)

        maxAgeInSeconds = 120

        anyHost()
    }
}
