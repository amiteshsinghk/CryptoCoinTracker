package com.amitesh.cryptocoin.crypto.data.networking

import com.amitesh.cryptocoin.core.data.networking.constructUrl
import com.amitesh.cryptocoin.core.data.networking.safeCall
import com.amitesh.cryptocoin.core.domain.util.NetworkError
import com.amitesh.cryptocoin.core.domain.util.Result
import com.amitesh.cryptocoin.core.domain.util.map
import com.amitesh.cryptocoin.crypto.data.mappers.toCoin
import com.amitesh.cryptocoin.crypto.data.mappers.toCoinPrice
import com.amitesh.cryptocoin.crypto.data.networking.dto.CoinsHistoryDto
import com.amitesh.cryptocoin.crypto.data.networking.dto.CoinsResponseDto
import com.amitesh.cryptocoin.crypto.domain.Coin
import com.amitesh.cryptocoin.crypto.domain.CoinDataSource
import com.amitesh.cryptocoin.crypto.domain.CoinPrice
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.time.ZoneId
import java.time.ZonedDateTime

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

    override suspend fun getCoinHistory(
        coinId: String,
        start: ZonedDateTime,
        end: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError> {
        val startMillis = start.withZoneSameInstant(
            ZoneId.of("UTC")
        ).toInstant().toEpochMilli()
        val endMillis = end.withZoneSameInstant(
            ZoneId.of("UTC")
        ).toInstant().toEpochMilli()
       return safeCall<CoinsHistoryDto> {
            httpClient.get(
                urlString = constructUrl("/assets/$coinId/history")
            ){
                parameter("interval", "h6")
                parameter("start", startMillis)
                parameter("end", endMillis)
            }
        }.map {
            it.data.map { coinPriceDto ->
                coinPriceDto.toCoinPrice()
            }
        }
    }
}