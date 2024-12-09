package com.example.wsc2023day2paper1.api

import com.example.wsc2023day2paper1.models.ConfirmTicket
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class PostTotalPrice {
    fun postFunction(bookingReference: String, totalPrice: Double): Boolean {
        val url = URL("http://10.0.2.2:5153/Airlines/postprice/$bookingReference/$totalPrice")

        try {
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = "POST"
            con.setRequestProperty("Content-Type", "application/json; utf-8")
            con.setRequestProperty("Accept", "application/json")
            con.doOutput = true

            val json = Json.encodeToString(totalPrice)
            val os = OutputStreamWriter(con.outputStream)

            os.write(json)
            os.flush()
            os.close()

            val status = con.responseCode
            if (status == 200) {
                return true

            } else {
                return false
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }

}