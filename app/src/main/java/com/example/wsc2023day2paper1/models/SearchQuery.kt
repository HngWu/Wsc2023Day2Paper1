package com.example.wsc2023day2paper1.models

import kotlinx.serialization.Serializable

@Serializable
data class SearchQuery(
    val from: String,
    val to: String,
    val departDate: String,
    val returnDate: String? = null,
    val noOfAdults: Int,
    val noOfChildren: Int
)
