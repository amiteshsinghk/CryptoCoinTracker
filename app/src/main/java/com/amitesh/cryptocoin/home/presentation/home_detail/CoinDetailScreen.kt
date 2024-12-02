package com.amitesh.cryptocoin.home.presentation.home_detail

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amitesh.cryptocoin.R
import com.amitesh.cryptocoin.home.presentation.home_detail.component.CardInfo
import com.amitesh.cryptocoin.home.presentation.home_detail.line_chart.ChartStyle
import com.amitesh.cryptocoin.home.presentation.home_detail.line_chart.DataPoint
import com.amitesh.cryptocoin.home.presentation.home_detail.line_chart.LineChart
import com.amitesh.cryptocoin.home.presentation.home_list.CoinListState
import com.amitesh.cryptocoin.home.presentation.home_list.components.previewCoin
import com.amitesh.cryptocoin.home.presentation.models.toDisplayableNumber
import com.amitesh.cryptocoin.ui.theme.AppTheme
import com.amitesh.cryptocoin.ui.theme.greenBackground

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CoinDetailScreen(
    state:CoinListState,
    modifier: Modifier
){
    val contentColor = if(isSystemInDarkTheme()){
        Color.White
    } else Color.Black
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (state.selectedCoin != null){
        val coin = state.selectedCoin
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(
                    id = coin.image
                ),
                contentDescription = coin.name,
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = coin.name,
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                color = contentColor
            )
            Text(
                text = coin.symbol,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                color = contentColor
            )

            FlowRow (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                horizontalArrangement = Arrangement.Center
            ){
                CardInfo(
                    title = stringResource(id = R.string.market_cap),
                    formattedText = "$ ${coin.marketCapUsd.formatted}",
                    icon = ImageVector.vectorResource(id = R.drawable.stock)
                )

                CardInfo(
                    title = stringResource(id = R.string.price),
                    formattedText = "$ ${coin.priceUsd.formatted}",
                    icon = ImageVector.vectorResource(id = R.drawable.dollar)
                )
                val absoluteChange =(coin.priceUsd.value * (coin.changePercent24Hr.value/100)).toDisplayableNumber()
                val isPositive = coin.changePercent24Hr.value > 0
                val contentColors = if(isPositive) {
                    if (isSystemInDarkTheme())Color.Green else greenBackground
                } else{
                    MaterialTheme.colorScheme.error
                }

                CardInfo(
                    title = stringResource(id = R.string.change_last_24_hrs),
                    formattedText = absoluteChange.formatted,
                    icon = if(isPositive) {
                        ImageVector.vectorResource(id = R.drawable.trending)
                    } else{
                        ImageVector.vectorResource(id = R.drawable.trending_down)
                    },
                    contentColor = contentColors
                )
            }
            AnimatedVisibility(
                visible = coin.coinPriceHistory.isNotEmpty()
            ) {
                var selectedDataPoint by remember{
                    mutableStateOf<DataPoint?>(null)
                }
                var labelWidth by remember {
                    mutableFloatStateOf(0f)
                }
                var totalChartWidth by remember{
                    mutableFloatStateOf(0f)
                }
                val amountOfVisibleDataPoints = if(labelWidth > 0){
                    ((totalChartWidth -2.5 * labelWidth) / labelWidth).toInt()
                } else {
                    0
                }

                val startIndex = (coin.coinPriceHistory.lastIndex - amountOfVisibleDataPoints)
                    .coerceAtLeast(0)
                LineChart(
                    dataPoints = coin.coinPriceHistory,
                    style = ChartStyle(
                        chartLineColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.secondary,
                        selectedColor = MaterialTheme.colorScheme.primary,
                        helperLineThickness =5f,
                        axisLineThicknessPx = 5f,
                        labelFontSize = 14.sp,
                        minYLabelSpacing = 25.dp,
                        verticalPadding = 8.dp,
                        horizontalPadding = 8.dp,
                        xAxisLabelSpacing =8.dp
                    ),
                    visibleDataPointsIndices = startIndex..coin.coinPriceHistory.lastIndex,
                    unit = "$",
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(16 / 9f)
                        .onSizeChanged { totalChartWidth = it.width.toFloat() },
                    selectedDataPoint = selectedDataPoint,
                    onSelectedDataPoint = {
                        Log.d("LineChart","CoinDetailScreen:: onSelectedDataPoint $it")
                        selectedDataPoint = it
                                          },
                    onXLabelWidthChange = {
                        labelWidth = it
                    }

                )
            }

        }
    }
}

@PreviewLightDark
@Composable
private fun CoinDetailScreenPreview(){
    AppTheme {
        CoinDetailScreen(
            state = CoinListState(
                selectedCoin = previewCoin
            ),
            modifier = Modifier.background(
                MaterialTheme.colorScheme.background
            )
        )

    }
}