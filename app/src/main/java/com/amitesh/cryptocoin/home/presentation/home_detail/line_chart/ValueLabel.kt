package com.amitesh.cryptocoin.home.presentation.home_detail.line_chart

import java.text.NumberFormat
import java.util.Locale

data class ValueLabel(
    val value: Float, // value of the coin
    val unit: String // unit of the coin
){ //Calculation to displaying total 5 digit in label
    fun formatted(): String{
        val formatter = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
            val fractionDigits = when {
                value > 1000 -> 0
                value in 2f..999f -> 2
                else -> 3
            }
            maximumFractionDigits = fractionDigits
            minimumFractionDigits =0
        }
        return "${formatter.format(value)} ${unit}"
    }
}
