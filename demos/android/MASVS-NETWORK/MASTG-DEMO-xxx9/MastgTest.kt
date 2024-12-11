package org.owasp.mastestapp

import android.content.Context
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

class MastgTest(private val context: Context) {

    fun mastgTest(): String {
        var result = ""

        runBlocking {
            withContext(Dispatchers.IO) {
                try {
                    // Use the default SSLSocketFactory
                    val sslSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory

                    // Connect to the server using SSLSocket
                    val host = "wrong.host.badssl.com"
                    val port = 443
                    val socket = sslSocketFactory.createSocket(host, port) as SSLSocket

                    // Start the handshake
                    socket.startHandshake()

                    // Send an HTTP GET request
                    val request = "GET / HTTP/1.1\r\nHost: $host\r\nConnection: close\r\n\r\n"
                    val outputStream = socket.outputStream
                    outputStream.write(request.toByteArray())
                    outputStream.flush()

                    // Read the response
                    val inputStream = socket.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val response = reader.readText()

                    // Update the sensitive string with the response
                    result = "Connection Successful: ${response.substring(0, minOf(200, response.length))}"
                } catch (e: Exception) {
                    // Log the error and update sensitive string
                    e.printStackTrace()
                    result = "Connection Failed: ${e::class.simpleName} - ${e.message}"
                }
            }
        }

        return result
    }
}