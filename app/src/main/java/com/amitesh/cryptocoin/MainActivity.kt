package com.amitesh.cryptocoin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amitesh.cryptocoin.core.presentation.util.ObserveAsEvents
import com.amitesh.cryptocoin.core.presentation.util.toString
import com.amitesh.cryptocoin.crypto.presentation.coin_detail.CoinDetailScreen
import com.amitesh.cryptocoin.crypto.presentation.coin_list.CoinListEvent
import com.amitesh.cryptocoin.crypto.presentation.coin_list.CoinListScreen
import com.amitesh.cryptocoin.crypto.presentation.coin_list.CoinListViewModel
import com.amitesh.cryptocoin.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel = koinViewModel<CoinListViewModel>()
                    val state = viewModel.state.collectAsStateWithLifecycle()
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
                    when{
                        state.value.selectedCoin != null -> {
                            CoinDetailScreen(
                                state = state.value,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        else -> {
                            CoinListScreen(
                                state = state.value,
                                modifier = Modifier.padding(innerPadding),
                                onAction = {
                                    viewModel.onAction(it)
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}

