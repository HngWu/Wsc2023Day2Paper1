package com.example.wsc2023day2paper1.models

import kotlinx.serialization.Serializable


@Serializable
data class BookingDetails(
    var bookingReference: String,
    var totalCost : Double,
    var economyPrice: Double,
    var departureIata: String,
    var departureDate: String,
    var departureTime: String,
    var arrivalDate : String,
    var arrivalTime : String,
    var totalTravelingTime: String,
    var ticket: List<TicketToConfirm>,
    var businessCapacity: Double,
    var economyCapacity: Double,
    var schedule : List<temSchedule>
)