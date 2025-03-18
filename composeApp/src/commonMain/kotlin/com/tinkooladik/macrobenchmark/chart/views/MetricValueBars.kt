package com.tinkooladik.macrobenchmark.chart.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.math.max

private val ITEM_COLOR_BEFORE = Color(0xFF5C8CBE)
private val ITEM_COLOR_AFTER = Color(0xFF3D7BB9)

@Composable
fun MetricValueBars(label: String, before: Double, after: Double, lowerBetter: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Box(modifier = Modifier.height(120.dp).width(40.dp)) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                fun scale(value: Double, reference: Double): Float {
                    val ratio =
                        if (reference == 0.0) 1.0 else value / reference
                    return (ratio * size.height).toFloat()
                }

                val referenceValue = max(before, after) // Normalize based on the larger value
                val beforeHeight = scale(before, referenceValue)
                val afterHeight = scale(after, referenceValue)

                // Before Value
                drawRoundRect(
                    color = ITEM_COLOR_BEFORE,
                    topLeft = Offset(0f, size.height - beforeHeight),
                    size = Size(
                        size.width / 2,
                        beforeHeight
                    ) // Use exactly half the width
                )

                // After Value
                drawRoundRect(
                    color = ITEM_COLOR_AFTER,
                    topLeft = Offset(
                        size.width / 2,
                        size.height - afterHeight
                    ), // No space added
                    size = Size(
                        size.width / 2,
                        afterHeight
                    ) // Use exactly half the width
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(label, fontSize = 14.sp)
        Text("Before: ${String.format("%.1f", before)}", fontSize = 10.sp)
        Text(
            "After: ${String.format("%.1f", after)}",
            fontSize = 10.sp
        )
        val differencePercent = (if (before != 0.0) {
            ((after - before) / before) * 100
        } else {
            0.0 // Avoid division by zero
        }).let {
            val sign = when {
                after > before -> "+"
                after < before -> "-"
                else -> ""
            }
            "$sign${String.format("%.1f", abs(it))}"
        }
        Spacer(
            modifier = Modifier.height(1.dp).width(150.dp)
                .background(Color(0xFFDEDCDC))
        )
        Text("Difference: $differencePercent%", fontSize = 12.sp)

        val thumb = when (lowerBetter) {
            true -> if (after < before) "\uD83D\uDC4D" else "\uD83D\uDC4E"
            false -> if (after > before) "\uD83D\uDC4D" else "\uD83D\uDC4E"
        }
        Text(thumb, modifier = Modifier.padding(top = 10.dp))
    }
}
