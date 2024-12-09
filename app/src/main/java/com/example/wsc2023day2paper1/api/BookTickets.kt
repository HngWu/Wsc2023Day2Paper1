package com.example.wsc2023day2paper1.api

import android.os.Handler
import android.os.Looper
import com.example.wsc2023day2paper1.models.Schedule
import com.example.wsc2023day2paper1.models.SearchQuery
import com.example.wsc2023day2paper1.models.Ticket
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class BookTickets {
    fun postFunction(ticketList: List<Ticket>): String? {
        val url = URL("http://10.0.2.2:5153/Airlines/tickets")

        try {
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = "POST"
            con.setRequestProperty("Content-Type", "application/json; utf-8")
            con.setRequestProperty("Accept", "application/json")
            con.doOutput = true

            val json = Json.encodeToString(ticketList)
            val os = OutputStreamWriter(con.outputStream)

            os.write(json)
            os.flush()
            os.close()

            val status = con.responseCode
            if (status == 200) {

                val reader = BufferedReader(InputStreamReader(con.inputStream))
                val jsonData = reader.use { it.readText() }
                reader.close()

                val objectList = Json.decodeFromString<String>(jsonData)

                return objectList

            }
        }catch (e: Exception) {
            return null
        }
        return null
    }



}