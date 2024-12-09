package com.example.wsc2023day2paper1.models

import kotlinx.serialization.Serializable

@Serializable
data class Ticket(
    val scheduleId: Int,
    val cabinType: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val passportNumber: String,
    val passportCountryId: String,
    val TicketType: String,
    val isTransfer: Boolean? = null,
    val transferScheduleId: Int? = null
)