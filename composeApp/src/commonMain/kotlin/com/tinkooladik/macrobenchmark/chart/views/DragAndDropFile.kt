package com.tinkooladik.macrobenchmark.chart.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetDropEvent
import java.io.File
import javax.swing.JPanel

@Composable
fun DragAndDropFile(text: String, onFileDropped: (File) -> Unit) {
    var message by remember { mutableStateOf("No file uploaded yet") }

    Column(
        modifier = Modifier
            .width(400.dp)
            .height(300.dp)
            .padding(20.dp)
            .clip(RoundedCornerShape(10.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Drag & Drop `$text` JSON file here",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(top = 20.dp)
        )

        SwingPanel(
            factory = {
                JPanel().apply {
                    isOpaque = false
                    dropTarget = object : DropTarget() {
                        override fun drop(evt: DropTargetDropEvent) {
                            evt.acceptDrop(DnDConstants.ACTION_COPY)
                            val droppedFiles =
                                evt.transferable.getTransferData(DataFlavor.javaFileListFlavor) as? List<File>
                            val file = droppedFiles?.firstOrNull()
                            if (file != null) {
                                message = "`$text` file: ${file.name}"
                                onFileDropped(file)
                            } else {
                                message = "Invalid `$text` file"
                            }
                            evt.dropComplete(true)
                        }
                    }
                }
            },
            background = Color(0xFFCFF9FF),
            modifier = Modifier.fillMaxWidth().height(180.dp).padding(20.dp)
        )

        Text(message, fontSize = 16.sp, color = Color.Black)
    }
}
