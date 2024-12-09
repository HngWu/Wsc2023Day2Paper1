package com.example.wsc2023day2paper1.api

import com.example.wsc2023day2paper1.models.TicketToConfirm
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class GetBookedTickets {
    fun getFunction(bookingReference: String): MutableList<TicketToConfirm>? {
        val url = URL("http://10.0.2.2:5153/Airlines/getbookedtickets/$bookingReference")

        try {
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.setRequestProperty("Content-Type", "application/json; utf-8")
            con.setRequestProperty("Accept", "application/json")
            //con.connectTimeout = 1000

            val status = con.responseCode
            if (status == 200) {
                val reader = BufferedReader(InputStreamReader(con.inputStream))
                val jsonData = reader.use { it.readText() }
                reader.close()

                val objectList = Json.decodeFromString<List<TicketToConfirm>>(jsonData) as MutableList<TicketToConfirm>?

                return objectList
            }
            con.disconnect()
        } catch (e: Exception) {
            return null
        }
        return null
    }
}