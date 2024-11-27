package com.amitesh.cryptocoin.di

import com.amitesh.cryptocoin.core.data.networking.HttpClientFactory
import com.amitesh.cryptocoin.crypto.data.networking.RemoteCoinDataSource
import com.amitesh.cryptocoin.crypto.domain.CoinDataSource
import com.amitesh.cryptocoin.crypto.presentation.coin_list.CoinListViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { HttpClientFactory.create(CIO.create()) }
    singleOf(::RemoteCoinDataSource).bind<CoinDataSource>()

    viewModelOf(::CoinListViewModel)

}