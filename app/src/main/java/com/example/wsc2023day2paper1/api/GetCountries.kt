package com.example.wsc2023day2paper1.api

import android.R
import com.example.wsc2023day2paper1.models.Schedule
import com.example.wsc2023day2paper1.models.SearchQuery
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class GetCountries {
    fun postFunction() : MutableList<String>? {
        val url = URL("http://10.0.2.2:5153/Airlines/countries")

        try {
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.setRequestProperty("Content-Type", "application/json; utf-8")
            con.setRequestProperty("Accept", "application/json")
            //con.doOutput = true



            val status = con.responseCode
            if (status == 200) {
                val reader = BufferedReader(InputStreamReader(con.inputStream))
                val jsonData = reader.use { it.readText() }
                reader.close()

                val objectList = Json.decodeFromString<List<String>>(jsonData) as MutableList<String>?

                return objectList

            }
            else
            {
                return null
            }
        }catch (e: Exception) {
            return null
        }
    }

}