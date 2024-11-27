package com.amitesh.cryptocoin.crypto.presentation.coin_list

import com.amitesh.cryptocoin.core.domain.util.NetworkError

sealed interface CoinListEvent {
    data class CoinListError(val error: NetworkError) : CoinListEvent
}