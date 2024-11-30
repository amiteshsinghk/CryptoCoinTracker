package com.amitesh.cryptocoin.home.presentation.home_list

import com.amitesh.cryptocoin.home.presentation.models.CoinUi

sealed interface ItemListAction {
    data class OnItemClick(val coinUi: CoinUi) : ItemListAction
}