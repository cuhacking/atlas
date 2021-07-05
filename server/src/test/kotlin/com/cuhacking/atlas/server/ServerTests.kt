package com.cuhacking.atlas.server

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.SerializationException
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.test.assertEquals

class ServerTests {
    private val data = Paths.get("src/test/resources/data.json")
    private val attr = Files.readAttributes(data, BasicFileAttributes::class.java)
    private val lastModified =
        ZonedDateTime.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.of("GMT"))
            .format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH))

    @Test
    fun `request for data returns content and last-modified header`() =
        withTestApplication(moduleFunction = {
            install(AutoHeadResponse)
            dataModuleFactory(data)
        }) {
            with(handleRequest(HttpMethod.Get, "/")) {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(data.toFile().readText(), response.content)
                assertEquals(lastModified, response.headers["Last-Modified"])
            }
            with(handleRequest(HttpMethod.Head, "/")) {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(lastModified, response.headers["Last-Modified"])
            }
        }

    @Test(expected = RuntimeException::class)
    fun `server throws on invalid data content`() {
        val server = Server()
        server.parse(listOf("-t", "src/test/resources/bad.json"))
    }
}
