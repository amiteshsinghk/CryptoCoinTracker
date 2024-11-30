package com.amitesh.cryptocoin.home.presentation.home_detail.line_chart

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amitesh.cryptocoin.home.domain.CoinPrice
import com.amitesh.cryptocoin.ui.theme.AppTheme
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun LineChart(
    dataPoints: List<DataPoint>,
    style: ChartStyle,
    visibleDataPointsIndices: IntRange,
    unit: String,
    modifier: Modifier = Modifier,
    selectedDataPoint: DataPoint? = null,
    onSelectedDataPoint: (DataPoint) -> Unit = {},
    onXLabelWidthChange: (Float) -> Unit = {},
    showHelperLines: Boolean = true
) {
    val textStyle = LocalTextStyle.current.copy(
        fontSize = style.labelFontSize
    )

    val visibleDataPoints = remember(dataPoints, visibleDataPointsIndices) {
        dataPoints.slice(visibleDataPointsIndices)
    }

    val maxYValue = remember(visibleDataPoints) {
        visibleDataPoints.maxOfOrNull { it.y } ?: 0f
    }

    val minYValue = remember(visibleDataPoints) {
        visibleDataPoints.minOfOrNull { it.y } ?: 0f
    }
    val measurer = rememberTextMeasurer()

    var xLabelWidth by remember {
        mutableFloatStateOf(0f)
    }
    LaunchedEffect(key1 = xLabelWidth) {
        onXLabelWidthChange(xLabelWidth)
    }

    val selectedDataPointIndex = remember(selectedDataPoint) {
        Log.d("LineChart","selectedDataPointIndexItem:: $selectedDataPoint :: index ==> ${dataPoints.indexOf(selectedDataPoint)} :: All Data Points ==> $dataPoints")
        dataPoints.indexOf(selectedDataPoint)
    }
    var drawPoints by remember {
        mutableStateOf(listOf<DataPoint>())
    }
    var isShowingDataPoints by remember {
        mutableStateOf(selectedDataPoint != null)
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(drawPoints, xLabelWidth){
                detectHorizontalDragGestures { change, _ ->
                   val newSelected = getSelectedDataPointIndex(
                       touchOffsetX =  change.position.x,
                       triggerWidth = xLabelWidth,
                       drawPoints = drawPoints
                   )
                    isShowingDataPoints = (newSelected + visibleDataPointsIndices.first) in
                            visibleDataPointsIndices
                    Log.d("LineChart","isShowingDataPoints $isShowingDataPoints")
                    if (isShowingDataPoints){
                        Log.d("LineChart","drawPoints[newSelected]:: ${dataPoints[newSelected]}")
                        onSelectedDataPoint(dataPoints[newSelected])
                    }
                }
            }
    ) {
        val minLabelSpacingY = style.minYLabelSpacing.toPx()   // Minimum spacing between Y-axis textView.
        val verticalPaddingPx = style.verticalPadding.toPx() // vertical padding between the start of the view or graph and the X label textView.
        val horizontalPaddingPx = style.horizontalPadding.toPx() // horizontal padding between the start of the view and the Y label textView.
        val xAxisLabelSpacingPx = style.xAxisLabelSpacing.toPx() // Minimum spacing between X-axis textView.

        val xLabelTextLayoutResults = visibleDataPoints.map {
            measurer.measure(
                text = it.xLabel,
                style = textStyle.copy(textAlign = TextAlign.Center)
            )
        }
        val maxXLabelWidth = xLabelTextLayoutResults.maxOfOrNull { it.size.width } ?: 0
        val maxXLabelHeight = xLabelTextLayoutResults.maxOfOrNull { it.size.height } ?: 0
        val maxXLabelLineCount = xLabelTextLayoutResults.maxOfOrNull { it.lineCount } ?: 0
        val xLabelLineHeight = if(maxXLabelLineCount>0)maxXLabelHeight / maxXLabelLineCount // Height of the x axis textView
                                else 0
        val viewPortHeightPx = size.height-
                (maxXLabelHeight + 2 * verticalPaddingPx +
                        xLabelLineHeight +xAxisLabelSpacingPx)


        /************************Y-axis text label calculation***********************************/
        /*
       * Here drawing Y axis text label.
       * labelViewPortHeightPx = viewPortHeightPx + xLabelLineHeight
       * viewPortHeightPx is the height of the graph.
       * xLabelLineHeight height value for the TextView.
       * Adding one TextView height to viewPortHeightPx centers the first and last labels on their coordinates in such a way that no clipping would occur.
       * */
        val labelViewPortHeightPx = viewPortHeightPx + xLabelLineHeight
        /*
        * Calculating the number of textview can drew in the Y-axis excluding the last textView as have added one extra textView height.
        * */
        val labelCountExcludingLastLabel = (labelViewPortHeightPx / (xLabelLineHeight + minLabelSpacingY)).toInt()
        /*
        * Calculating the amount of price change that we need to show in the text view.
        *   For ex
        *   maxYValue = 200
        *   minYValue = 50
        *   labelCount = 2
        *   valueIncrement = (200 - 50) / 2 = 75
        *  So the three text value will be 50, 125 and 200
        * */
        val valueIncrement = (maxYValue - minYValue) / labelCountExcludingLastLabel
        /*
        *  These are the string value which need to show in the textView label.
        * */
        val yLabels = (0..labelCountExcludingLastLabel).map {
            ValueLabel(
                value = minYValue - (it * valueIncrement),
                unit = unit

            )
        }
        /*
        * To measure the bounding box of y label textview
        * */
        val yLabelTextLayoutResults = yLabels.map{
            measurer.measure(
                text = it.formatted(),
                style = textStyle
            )
        }
        /*
        * Maximum width of Y level textView
        * */
        val maximumWidthOfYLabelText = yLabelTextLayoutResults.maxOfOrNull { it.size.width } ?: 0

/************************Graph Calculation***********************************/
        val viewPortTopY = verticalPaddingPx + xLabelLineHeight + 10f
        val viewPortRightX = size.width
        val viewPortBottomY = viewPortTopY + viewPortHeightPx
        val viewPortLeftX = 2f*horizontalPaddingPx + maximumWidthOfYLabelText

        /************************X-axis text label calculation***********************************/
        /*
        * x= viewPortLeftX + xAxisLabelSpacingPx / 2f + xLabelWidth *index
        * Here, the bottom label is drawn starting from the graph's `viewPortLeftX` (x-axis) and `viewPortBottomY` (y-axis).
        * xAxisLabelSpacingPx / 2f ensures equal spacing between the coordinate and TextView on both sides.
        * xLabelWidth * index offsets each label based on its size and index to prevent overlap.
        * y = viewPortBottomY + xAxisLabelSpacingPx
        * Here, xAxisLabelSpacingPx is used to provide the top margin between the graph and textView.
        * */
        xLabelWidth = maxXLabelWidth + xAxisLabelSpacingPx
        xLabelTextLayoutResults.forEachIndexed { index, textLayoutResult ->
            val x= viewPortLeftX + xAxisLabelSpacingPx / 2f +
                    xLabelWidth *index
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x= x,
                    y = viewPortBottomY + xAxisLabelSpacingPx
                ),
                color = if (index == selectedDataPointIndex) style.selectedColor else style.unselectedColor
            )
            /*
              * Drawing the vertical lines
              * */
            if (showHelperLines){
                drawLine(
                    color = if (selectedDataPointIndex == index) style.selectedColor else style.unselectedColor,
                    start = Offset(
                        x = x + textLayoutResult.size.width/2f,
                        y = viewPortBottomY
                    ),
                    end = Offset(
                        x= x + textLayoutResult.size.width/2f,
                        y= viewPortTopY
                    ),
                    strokeWidth = if (selectedDataPointIndex == index)style.helperLineThickness * 2 else style.helperLineThickness
                )
            }

            /*
            * Drawing Top text label
            * */

            if(selectedDataPointIndex == index){
                val selectedValue = ValueLabel(
                    value = visibleDataPoints[index].y,
                    unit = unit
                )
                val  valueResult =measurer.measure(
                    text = selectedValue.formatted(),
                    style = textStyle.copy(
                    color = style.selectedColor
                ),
                    maxLines = 1
                )
                val textViewPosition = if (selectedDataPointIndex == visibleDataPointsIndices.last){
                        x- valueResult.size.width
                } else{
                    x- valueResult.size.width / 2f
                } + textLayoutResult.size.width / 2f
                val isTextViewIsInVisibleRange = (size.width - textViewPosition).roundToInt() in 0 .. size.width.roundToInt()
                if (isTextViewIsInVisibleRange){
                    drawText(
                        textLayoutResult = valueResult,
                        topLeft = Offset(
                            x = textViewPosition +10f,
                            y = viewPortTopY - valueResult.size.height - 10f
                        ),
                    )
                }
            }
        }

        /*
         * Total Height for the label textview without the spacing between them.
         * Here 1 is added for the last textView which we need to include in the height.
         */
        val heightRequiredForLabels = xLabelLineHeight * (labelCountExcludingLastLabel + 1)
        /*
         * Remaining height i.e the space between two textView labels.
         */
        val remainingHeight = labelViewPortHeightPx - heightRequiredForLabels
        /*
        * Space between two textView labels.
        * */
        val spaceBetweenLabels = remainingHeight / labelCountExcludingLastLabel


        /*
        * Drawing the textView on each item of yLabels.
        * */
        yLabelTextLayoutResults.forEachIndexed { index, textLayoutResult ->
            /*
            * x is calculated so that the text is align in the left side to make it align to the right side, Here we are calculating the x value.
            * */
            val x = horizontalPaddingPx + maximumWidthOfYLabelText - textLayoutResult.size.width.toFloat()
            val y = viewPortTopY +
                    index * (xLabelLineHeight + spaceBetweenLabels) -
                    xLabelLineHeight / 2f
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = x,
                    y = y
                ),
                color = if (index == selectedDataPointIndex) style.selectedColor else style.unselectedColor
            )
            /*
            * Drawing the horizontal lines
            * */
            if (showHelperLines){
                drawLine(
                    color = style.unselectedColor,
                    start = Offset(
                        x = viewPortLeftX,
                        y = y + textLayoutResult.size.height.toFloat()/2f
                    ),
                    end = Offset(
                        x= viewPortRightX,
                        y= y + textLayoutResult.size.height.toFloat()/2f
                    ),
                    strokeWidth = style.helperLineThickness
                )
            }
        }
/*
* Draw points on graph
*viewPortLeftX - co-ordinate of starting point or x-axis or left edge.
*xLabelWidth -> width of the co-ordinate or the space between two vertical lines
* */
        drawPoints = visibleDataPointsIndices.map {
            val x = viewPortLeftX + (it - visibleDataPointsIndices.first) *
                    xLabelWidth + xLabelWidth / 2f
            // [minValue; maxValue] ->[0;1]
            val ratio = (dataPoints[it].y - minYValue) /(maxYValue - minYValue)
            val y = viewPortBottomY - (ratio * viewPortHeightPx)
            DataPoint(
                x = x,
                y = y,
                xLabel = dataPoints[it].xLabel
            )
        }

        val conPoint1 = mutableListOf<DataPoint>()
        val conPoint2 = mutableListOf<DataPoint>()
        for( i in 1 until drawPoints.size){
            val p0 = drawPoints[i - 1]
            val p1 = drawPoints[i]

            val x =(p1.x + p0.x) / 2f
            val y1 = p0.y
            val y2 = p1.y
            conPoint1.add(DataPoint(x, y1,""))
            conPoint2.add(DataPoint(x,y2,""))
        }

        val linePath = Path().apply{
            if(drawPoints.isNotEmpty()){
                moveTo(drawPoints.first().x, drawPoints.first().y)

                for(i in 1 until drawPoints.size){
                    cubicTo(
                        x1 = conPoint1[i-1].x,
                        y1 = conPoint1[i-1].y,
                        x2 = conPoint2[i-1].x,
                        y2 = conPoint2[i-1].y,
                        x3 = drawPoints[i].x,
                        y3 = drawPoints[i].y
                    )
                }
            }
        }

        drawPath(
            path = linePath,
            color = style.chartLineColor,
            style = Stroke(
                width = 5f,
                cap = StrokeCap.Round
            )
        )

        drawPoints.forEachIndexed { index, dataPoint ->
            if(isShowingDataPoints){
                val circleOffset = Offset(
                    x = dataPoint.x,
                    y = dataPoint.y
                )
                drawCircle(
                    color = style.selectedColor,
                    radius = 10f,
                    center = circleOffset
                )
                Log.d("LineChart","selectedDataPointIndex $selectedDataPointIndex:: index $index")
                if (selectedDataPointIndex == index){
                    Log.d("LineChart","insideIf")
                    drawCircle(
                        color = Color.White,
                        radius = 15f,
                        center = circleOffset
                    )
                    drawCircle(
                        color = style.selectedColor,
                        radius = 15f,
                        center = circleOffset,
                        style = Stroke(width = 3f)
                    )
                }

            }
        }
    }

}

private fun getSelectedDataPointIndex(
    touchOffsetX: Float,
    triggerWidth: Float,
    drawPoints: List<DataPoint>
): Int{
    val triggerRangeLeft = touchOffsetX - triggerWidth/2f
    val triggerRangeRight = touchOffsetX + triggerWidth/2f
    return drawPoints.indexOfFirst {
        it.x in triggerRangeLeft .. triggerRangeRight
    }
}


@Preview(widthDp = 1000)
@Composable
private fun LineChartPreview(widthDp: Dp = 1000.dp){
    AppTheme  {
        val coinHistory = remember {
            (1..20).map {
                CoinPrice(
                    priceUsd = Random.nextFloat() * 1000.0,
                    dateTime = ZonedDateTime.now().plusHours(it.toLong())
                )
            }
        }
        val style = ChartStyle(
            chartLineColor = Color.Black,
            unselectedColor = Color(0xFF7C7C7C),
            selectedColor = Color.Black,
            helperLineThickness = 1f,
            axisLineThicknessPx = 5f,
            labelFontSize = 14.sp,
            minYLabelSpacing = 25.dp,
            verticalPadding = 8.dp,
            horizontalPadding = 8.dp,
            xAxisLabelSpacing = 8.dp
        )

        val dataPont = remember {
            coinHistory.map {
                DataPoint(
                    x = it.dateTime.hour.toFloat(),
                    y = it.priceUsd.toFloat(),
                    xLabel = DateTimeFormatter.ofPattern("ha\nM/d")
                        .format(it.dateTime)
                )
            }
        }
        LineChart(
            dataPoints = dataPont,
            style = style,
            visibleDataPointsIndices = 0..19,
            unit = "$",
            modifier = Modifier
                .width(700.dp)
                .height(300.dp)
                .background(Color.White),
            selectedDataPoint = dataPont[1])
    }
}