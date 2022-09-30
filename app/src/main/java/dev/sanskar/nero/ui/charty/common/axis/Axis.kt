package dev.sanskar.nero.ui.charty.common.axis

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import java.text.DecimalFormat

internal fun DrawScope.yAxis(axisConfig: AxisConfig, maxValue: Float, isCandleChart: Boolean = false, labelColor: Color = Color.Black) {
    val graphYAxisEndPoint = size.height.div(4)
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 20f), 0f)
    val labelScaleFactor = maxValue.div(4)

    repeat(5) { index ->
        val yAxisEndPoint = graphYAxisEndPoint.times(index)
        if (axisConfig.showUnitLabels) {
            drawIntoCanvas {
                it.nativeCanvas.apply {
                    drawText(
                        getLabelText(labelScaleFactor.times(4.minus(index)), isCandleChart),
                        0F.minus(25),
                        yAxisEndPoint.minus(10),
                        Paint().apply {
                            textSize = size.width.div(30)
                            textAlign = Paint.Align.CENTER
                            color = labelColor.toArgb()
                        }
                    )
                }
            }
        }
        if (index != 0) {
            drawLine(
                start = Offset(x = 0f, y = yAxisEndPoint),
                end = Offset(x = size.width, y = yAxisEndPoint),
                color = axisConfig.xAxisColor,
                pathEffect = if (axisConfig.isAxisDashed) pathEffect else null,
                alpha = 0.1F,
                strokeWidth = size.width.div(200)
            )
        }
    }
}

private fun getLabelText(value: Float, isCandleChart: Boolean): String {
    val pattern = if (isCandleChart) "#" else "#.##"
    return DecimalFormat(pattern).format(value).toString()
}
