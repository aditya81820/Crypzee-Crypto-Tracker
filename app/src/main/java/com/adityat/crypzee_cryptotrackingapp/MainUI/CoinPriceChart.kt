package com.adityat.crypzee_cryptotrackingapp.MainUI

import android.graphics.Color
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.adityat.crypzee_cryptotrackingapp.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CoinPriceChart(
    data: List<Pair<Long, Double>>,
    valuesend: Boolean,
    onPriceSelected: (Double) -> Unit
) {
    val lineEntries = data.map { (timestamp, price) ->
        Entry(timestamp.toFloat(), price.toFloat())
    }

    val datasetColorInt = MaterialTheme.colorScheme.secondary.toArgb()
    val datasetColor = String.format("#%06X", 0xFFFFFF and datasetColorInt)

    val graphlineColor = if(!valuesend==true){ "#00ff00" }else{ "#eb0000"}


    AndroidView(factory = { context ->
        val chart = LineChart(context)

        val dataSet = LineDataSet(lineEntries, "Coin Price")
        dataSet.color = Color.parseColor(graphlineColor)
        dataSet.setDrawCircles(false)
        dataSet.lineWidth = 2f
        dataSet.setValueTextColor(Color.parseColor(datasetColor))

        chart.data = LineData(dataSet)

        chart.xAxis.isEnabled = false

        val yAxis = chart.axisLeft
        yAxis.textColor = Color.parseColor(datasetColor)
        yAxis.gridColor = Color.parseColor("#D3D3D3")
        chart.axisRight.isEnabled = false

        // Set up the MarkerView for tooltip display
        val markerView = object : MarkerView(context, R.layout.custom_marker_view) {
            override fun refreshContent(e: Entry?, highlight: Highlight?) {
                val date = Date(e?.x?.toLong() ?: 0L)
                // Format date and time
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                findViewById<TextView>(R.id.marker_text).text = format.format(date)

                // Call the onPriceSelected callback with the current price
                onPriceSelected(e?.y?.toDouble() ?: 0.0)

                super.refreshContent(e, highlight)
            }

            override fun getOffset(): MPPointF {
                return MPPointF(-(width / 2).toFloat(), -height.toFloat())
            }
        }
        chart.legend.textColor = Color.parseColor(datasetColor)

        chart.marker = markerView
        chart.invalidate()
        chart
    }, modifier = Modifier.fillMaxWidth().height(400.dp).padding(10.dp))
}
