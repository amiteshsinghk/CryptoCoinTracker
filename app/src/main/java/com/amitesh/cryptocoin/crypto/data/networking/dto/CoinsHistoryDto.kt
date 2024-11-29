package com.amitesh.cryptocoin.crypto.data.networking.dto

import com.amitesh.cryptocoin.core.domain.util.NetworkError
import com.amitesh.cryptocoin.core.domain.util.Result
import com.amitesh.cryptocoin.crypto.domain.Coin
import com.amitesh.cryptocoin.crypto.domain.CoinDataSource
import com.amitesh.cryptocoin.crypto.domain.CoinPrice
import io.ktor.client.HttpClient
import kotlinx.serialization.Serializable

@Serializable
data class CoinsHistoryDto(
    val data: List<CoinPriceDto>
)
