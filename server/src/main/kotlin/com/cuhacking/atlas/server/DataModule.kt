package com.cuhacking.atlas.server

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes
import java.time.ZoneId
import java.time.ZonedDateTime

fun dataModuleFactory(path: Path): Application.() -> Unit {
    val attr = Files.readAttributes(path, BasicFileAttributes::class.java)

    @Suppress("FunctionNaming")
    return fun Application.() {
        routing {
            get("/") {
                call.response.lastModified(
                    ZonedDateTime.ofInstant(
                        attr.lastModifiedTime().toInstant(), ZoneId.systemDefault()
                    )
                )
                call.respondFile(path.toFile())
            }
        }
    }
}
