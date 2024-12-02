package com.amitesh.cryptocoin.home.presentation.home_list

import com.amitesh.cryptocoin.core.domain.util.NetworkError

sealed interface ItemListEvent {
    data class ItemListError(val error: NetworkError) : ItemListEvent
}