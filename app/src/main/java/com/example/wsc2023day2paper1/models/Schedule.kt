package com.example.wsc2023day2paper1.models

import kotlinx.serialization.Serializable

@Serializable
data class Schedule (
    val id : Int,
    val date: String,
    val departureIata: String,
    val arrivalAirIata: String,
    val departureTime: String,
    val arrivalTime: String,
    val duration: Int,
    val price: Double,
    val isTransfer: Boolean,
    val transferScheduleId: Int? = null,
    val transferDate: String? = null,
    val transferDepartureIata: String? = null,
    val transferArrivalIata: String? = null,
    val transferDepartureTime: String? = null,
    val transferArrivalTime: String? = null,
    val transferDuration: Int? = null,
    val transferPrice: Double? = null
)