package com.amitesh.cryptocoin.core.navigation

import android.widget.Toast
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amitesh.cryptocoin.core.presentation.util.ObserveAsEvents
import com.amitesh.cryptocoin.core.presentation.util.toString
import com.amitesh.cryptocoin.crypto.presentation.coin_detail.CoinDetailScreen
import com.amitesh.cryptocoin.crypto.presentation.coin_list.CoinListAction
import com.amitesh.cryptocoin.crypto.presentation.coin_list.CoinListEvent
import com.amitesh.cryptocoin.crypto.presentation.coin_list.CoinListScreen
import com.amitesh.cryptocoin.crypto.presentation.coin_list.CoinListViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AdaptiveCoinListDetailPane(
    modifier: Modifier = Modifier,
    viewModel: CoinListViewModel = koinViewModel(),
){
    val state  by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    ObserveAsEvents(events = viewModel.events) {
        when (it) {
            is CoinListEvent.CoinListError -> {
                Toast.makeText(
                    context,
                    it.error.toString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
                CoinListScreen(
                    state = state,
                    modifier = Modifier,
                    onAction = {
                        viewModel.onAction(it)
                        when(it){
                            is CoinListAction.OnCoinClick -> {
                                navigator.navigateTo(
                                   pane = ListDetailPaneScaffoldRole.Detail
                                )
                            }
                        }
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane{ 
                CoinDetailScreen(
                    state = state,
                    modifier = modifier
                )
            }
        },
        modifier = modifier,
    ) 
}