package com.tinkooladik.macrobenchmark.chart.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tinkooladik.macrobenchmark.chart.benchmarkComparisonRules
import com.tinkooladik.macrobenchmark.chart.models.MetricChartItem

@Composable
fun MetricBarChart(items: List<MetricChartItem>) {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            items.forEach { item ->
                Text(item.title, fontSize = 18.sp, modifier = Modifier.padding(bottom = 4.dp))
                Text(
                    benchmarkComparisonRules[item.title] ?: "",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 36.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 48.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    item.values.forEach { (label, before, after) ->
                        MetricValueBars(label, before, after)
                    }
                }
                Spacer(
                    modifier = Modifier.height(1.dp).fillMaxWidth()
                        .background(Color(0xFFD2D2D2))
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}