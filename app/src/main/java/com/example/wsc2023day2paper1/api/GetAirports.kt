package com.example.wsc2023day2paper1.api

import java.net.HttpURLConnection
import java.net.URL
import kotlinx.serialization.json.Json
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

class GetAirports {
    fun getFunction(): MutableList<String>? {
        val url = URL("http://10.0.2.2:5153/Airlines/airports")

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

                val objectList = Json.decodeFromString<List<String>>(jsonData) as MutableList<String>?

                return objectList
            }
            con.disconnect()
        } catch (e: Exception) {
            return null
        }
        return null
    }

}