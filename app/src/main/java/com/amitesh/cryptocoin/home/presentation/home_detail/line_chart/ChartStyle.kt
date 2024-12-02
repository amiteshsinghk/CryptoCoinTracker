package com.amitesh.cryptocoin.home.presentation.home_detail.line_chart

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

//To configure the chart
data class ChartStyle(
    val chartLineColor: Color, // chart line color
    val unselectedColor: Color,// unselected item color
    val selectedColor: Color, // selected item color
    val helperLineThickness: Float, // vertical and horizontal line thickness
    val axisLineThicknessPx: Float, // Line for x and y axis
    val labelFontSize: TextUnit, // Font size of textview
    val minYLabelSpacing: Dp, // Minimum space between two text view in Y-axis
    val verticalPadding: Dp, // top padding of the graph
    val horizontalPadding: Dp, //  bottom padding of the graph
    val xAxisLabelSpacing: Dp, // space between two textview in X-axis
)
