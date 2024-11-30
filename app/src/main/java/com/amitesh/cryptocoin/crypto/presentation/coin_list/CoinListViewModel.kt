package com.amitesh.cryptocoin.crypto.presentation.coin_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amitesh.cryptocoin.core.domain.util.onError
import com.amitesh.cryptocoin.core.domain.util.onSuccess
import com.amitesh.cryptocoin.crypto.domain.CoinDataSource
import com.amitesh.cryptocoin.crypto.presentation.coin_detail.line_chart.DataPoint
import com.amitesh.cryptocoin.crypto.presentation.models.CoinUi
import com.amitesh.cryptocoin.crypto.presentation.models.toCoinUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CoinListViewModel(
    private val coinDataSource: CoinDataSource
): ViewModel() {

    private val _state = MutableStateFlow(CoinListState())
    val state =_state
        .onStart {
            loadCoins()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CoinListState()
        )

    private fun loadCoins() {
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true
            )
            }
            coinDataSource
                .getCoins()
                .onSuccess {coins->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            coins = coins.map { it.toCoinUi() }
                        )
                    }
                }
                .onError { error ->
                    _state.update {
                        it.copy(isLoading = false)
                    }
                    _event.send(CoinListEvent.CoinListError(error))
                }
        }
    }

    private  val _event = Channel<CoinListEvent>()
    val events = _event.receiveAsFlow()

    fun onAction(action: CoinListAction) {
        when(action){
            is CoinListAction.OnCoinClick -> {
               selectCoin(action.coinUi)
            }
        }
    }

    private fun selectCoin(coinUi: CoinUi){
        _state.update{
            it.copy(
                selectedCoin = coinUi
            )
        }
        viewModelScope.launch {
            coinDataSource.getCoinHistory(
                coinUi.id,
                start = ZonedDateTime.now().minusDays(6),
                end = ZonedDateTime.now()
            )
                .onSuccess { history ->
                   val dataPoints = history
                       .sortedBy { it.dateTime }
                       .map {
                           DataPoint(
                               x= it.dateTime.hour.toFloat(),
                               y = it.priceUsd.toFloat(),
                               xLabel = DateTimeFormatter
                                   .ofPattern("ha\nM/d")
                                   .format(it.dateTime)
                           )
                       }
                    _state.update {
                        it.copy(
                            selectedCoin = it.selectedCoin?.copy(
                                coinPriceHistory = dataPoints
                            )
                        )
                    }
                }
                .onError { error ->
                   _event.send(CoinListEvent.CoinListError(error))
                }
        }
    }
}