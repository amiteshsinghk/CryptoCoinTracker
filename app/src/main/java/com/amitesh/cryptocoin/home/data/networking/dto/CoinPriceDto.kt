package com.amitesh.cryptocoin.home.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoinPriceDto(
    val time: Long,
    val priceUsd: Double

)
