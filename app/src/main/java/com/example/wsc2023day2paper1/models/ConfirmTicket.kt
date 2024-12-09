package com.example.wsc2023day2paper1.models

import kotlinx.serialization.Serializable

@Serializable
data class ConfirmTicket (
    val ticketId : Int,
    val seatNumber: String,
)