package com.example.wsc2023day2paper1.models

import kotlinx.serialization.Serializable

@Serializable
data class TicketToConfirm(
    val id: Int,
    val scheduleId: Int,
    val cabinTypeId: Int,
    val firstname: String,
    val lastname: String,
    val email: String?,
    val phone: String,
    val passportNumber: String,
    val passportCountryId: Int,
    val bookingReference: String,
    val confirmed: Boolean,
    val seatNo: String?,
    val ticketTypeId: Int
)