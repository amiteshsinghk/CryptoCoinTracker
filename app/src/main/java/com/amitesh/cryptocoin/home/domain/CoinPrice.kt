package com.amitesh.cryptocoin.home.domain

import java.time.ZonedDateTime

data class CoinPrice(
    val dateTime: ZonedDateTime,
    val priceUsd: Double
)
