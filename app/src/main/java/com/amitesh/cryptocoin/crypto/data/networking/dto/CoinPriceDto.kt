package com.amitesh.cryptocoin.crypto.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class CoinPriceDto(
    val time: Long,
    val priceUsd: Double

)
