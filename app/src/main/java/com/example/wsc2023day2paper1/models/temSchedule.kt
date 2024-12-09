package com.example.wsc2023day2paper1.models

import kotlinx.serialization.Serializable

@Serializable
data class temSchedule (
    var id: Int,
    var date: String?,
    var time: String?,
    var aircraftId: Int,
    var routeId: Int,
    var economyPrice: Double,
    var confirmed: Boolean,
    var flightNumber: String?,
    var departureIata: String,
    var departureDate: String,
    var departureTime: String,
    var arrivalDate: String,
    var arrivalTime: String,
    var travelingTime: String
)