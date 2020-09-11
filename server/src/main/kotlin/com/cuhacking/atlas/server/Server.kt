package com.cuhacking.atlas.server

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.path
import io.github.dellisd.spatialk.geojson.FeatureCollection.Companion.toFeatureCollection
import io.ktor.server.engine.*
import io.ktor.server.netty.*

class Server : CliktCommand() {
    private val file by argument(help = "Path to directory containing data files").path(
        mustExist = true,
        canBeDir = false
    )

    @Suppress("MagicNumber")
    val port by option("--port", help = "Port for the server to listen on").int().default(8080)

    override fun run() {
        verifyData()
        embeddedServer(Netty, port, module = dataModuleFactory(file)).start(wait = true)
    }

    internal fun verifyData() {
        // Will throw error if file is not a valid FeatureCollection
        file.toFile().readText().toFeatureCollection()
    }
}

fun main(args: Array<String>) = Server().main(args)
