package com.tinkooladik.macrobenchmark.chart.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tinkooladik.macrobenchmark.chart.models.MetricChartItem

@Composable
fun BenchmarkChart(items: List<MetricChartItem>) {
    items.sortedBy {
        it.rule.priority
    }
        .forEach { item ->
            Text(item.title, fontSize = 16.sp, modifier = Modifier.padding(bottom = 4.dp))
            Text(
                item.rule.description,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 36.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                item.values.forEach { (label, before, after) ->
                    MetricValueBars(label, before, after, item.rule.lowerBetter)
                }
            }
            Spacer(
                modifier = Modifier.height(1.dp).fillMaxWidth()
                    .background(Color(0xFFD2D2D2))
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
}
