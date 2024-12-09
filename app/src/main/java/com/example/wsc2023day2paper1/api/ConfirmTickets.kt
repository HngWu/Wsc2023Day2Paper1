package com.example.wsc2023day2paper1.api

import android.os.Handler
import android.os.Looper
import com.example.wsc2023day2paper1.models.ConfirmTicket
import com.example.wsc2023day2paper1.models.Ticket
import com.example.wsc2023day2paper1.models.TicketToConfirm
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class ConfirmTickets {
    fun postFunction(ticketList: List<ConfirmTicket>): Boolean {
        val url = URL("http://10.0.2.2:5153/Airlines/confirmticket")

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