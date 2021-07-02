package com.cuhacking.atlas.server

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.path
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.everit.json.schema.ValidationException
import org.everit.json.schema.loader.SchemaLoader
import org.json.JSONObject
import org.json.JSONTokener
import kotlin.io.path.readText

class Server : CliktCommand() {
    private val file by argument(help = "Path to directory containing data files").path(
        mustExist = true,
        canBeDir = false
    )

    @Suppress("MagicNumber")
    val port by option("--port", help = "Port for the server to listen on").int().default(8080)

    private val test by option(
        "--test",
        "-t",
        help = "Validate data against the schema without starting the server"
    ).flag()

    override fun run() {
        if (!verifyData()) {
            throw RuntimeException()
        }

        if (!test) {
            embeddedServer(CIO, port, module = {
                install(AutoHeadResponse)
                install(CallLogging)
                dataModuleFactory(file)
            }).start(wait = true)
        }
    }

    private fun verifyData(): Boolean {
        var valid = true

        val stream = javaClass.classLoader.getResourceAsStream("data.schema.json")
        val rawSchema = JSONObject(JSONTokener(stream))
        val schema = SchemaLoader.load(rawSchema)

        val json = file.readText()

        try {
            schema.validate(JSONObject(json))
        } catch (e: ValidationException) {
            valid = false
            println(e.message)
            e.causingExceptions.map(ValidationException::message).forEach(::println)
        } catch (e: Exception) {
            println(e)
        }

        return valid
    }
}

fun main(args: Array<String>) = Server().main(args)
