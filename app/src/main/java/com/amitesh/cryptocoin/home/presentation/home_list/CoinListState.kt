package com.amitesh.cryptocoin.home.presentation.home_list

import androidx.compose.runtime.Immutable
import com.amitesh.cryptocoin.home.presentation.models.CoinUi

@Immutable// This will tell the compiler this class will not change, If it change
// the whole instance will replace.
data class CoinListState(
    val isLoading: Boolean = false,
    val coins: List<CoinUi> = emptyList(),
    val selectedCoin: CoinUi? = null
)
