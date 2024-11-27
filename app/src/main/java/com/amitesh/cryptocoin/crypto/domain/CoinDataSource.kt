package com.amitesh.cryptocoin.crypto.domain

import com.amitesh.cryptocoin.core.domain.util.NetworkError
import com.amitesh.cryptocoin.core.domain.util.Result

interface CoinDataSource {
    suspend fun getCoins(): Result<List<Coin>, NetworkError>
}