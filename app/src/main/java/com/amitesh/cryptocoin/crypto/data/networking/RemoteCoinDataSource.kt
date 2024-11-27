package com.amitesh.cryptocoin.crypto.data.networking

import com.amitesh.cryptocoin.core.data.networking.constructUrl
import com.amitesh.cryptocoin.core.data.networking.safeCall
import com.amitesh.cryptocoin.core.domain.util.NetworkError
import com.amitesh.cryptocoin.core.domain.util.Result
import com.amitesh.cryptocoin.core.domain.util.map
import com.amitesh.cryptocoin.crypto.data.mappers.toCoin
import com.amitesh.cryptocoin.crypto.data.networking.dto.CoinsResponseDto
import com.amitesh.cryptocoin.crypto.domain.Coin
import com.amitesh.cryptocoin.crypto.domain.CoinDataSource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class RemoteCoinDataSource(
    private val httpClient: HttpClient
) : CoinDataSource {
    override suspend fun getCoins(): Result<List<Coin>, NetworkError> {
        return safeCall<CoinsResponseDto> {
            httpClient.get(
                urlString = constructUrl("/assets")
            )
        }.map {
            it.data.map { coinDto ->
                coinDto.toCoin()
            }

        }
    }
}