package com.amitesh.cryptocoin.crypto.presentation.coin_list

import com.amitesh.cryptocoin.crypto.presentation.models.CoinUi

sealed interface CoinListAction {
    data class OnCoinClick(val coinUi: CoinUi) : CoinListAction
}