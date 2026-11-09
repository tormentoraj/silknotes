package com.silknotes.presentation.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.drawPath
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.ArrowBack
import androidx.compose.material3.icons.filled.Add
import androidx.compose.material3.icons.filled.Brush
import androidx.compose.material3.icons.filled.Clear
import androidx.compose.material3.icons.filled.Close
import androidx.compose.material3.icons.filled.ColorLens
import androidx.compose.material3.icons.filled.Delete
import androidx.compose.material3.icons.filled.Download
import androidx.compose.material3.icons.filled.Edit
import androidx.compose.material3.icons.filled.FormatColorFill
import androidx.compose.material3.icons.filled.NavigateNext
import androidx.compose.material3.icons.filled.NavigateBefore
import androidx.compose.material3.icons.filled.PanTool
import androidx.compose.material3.icons.filled.Redo
import androidx.compose.material3.icons.filled.Share
import androidx.compose.material3.icons.filled.Undo
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsStateWithLifecycle
import com.silknotes.R
import com.silknotes.domain.model.Stroke
import com.silknotes.domain.model.StrokePoint
import com.silknotes.domain.model.ToolType
import com.silknotes.presentation.theme.SilkNotesTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    noteId: Long,
    folderId: Long = 0,
    onBackPress: () -> Unit,
    onExportPng: () -> Unit = {},
    onExportPdf: () -> Unit = {},
    onShare: () -> Unit = {}
) {
    val viewModel: EditorViewModel = viewmodel()
    val strokes by viewModel.strokes.collectAsStateWithLifecycle()
    val selectedTool by viewModel.selectedTool.collectAsStateWithLifecycle()
    val strokeColor by viewModel.strokeColor.collectAsStateWithLifecycle()
    val strokeWidth by viewModel.strokeWidth.collectAsStateWithLifecycle()
    val pressureSensitivity by viewModel.pressureSensitivity.collectAsStateWithLifecycle()
    val palmRejection by viewModel.palmRejection.collectAsStateWithLifecycle()
    val backgroundColor by viewModel.backgroundColor.collectAsStateWithLifecycle()
    val currentPageIndex by viewModel.currentPageIndex.collectAsStateWithLifecycle()
    val pages by viewModel.pages.collectAsStateWithLifecycle()
    val note by viewModel.note.collectAsStateWithLifecycle()
    val canUndo by viewModel.undoStack.collectAsStateWithLifecycle().map { it.isNotEmpty() }
    val canRedo by viewModel.redoStack.collectAsStateWithLifecycle().map { it.isNotEmpty() }

    var panOffset by remember { mutableStateOf(Offset.Zero) }
    var zoom by remember { mutableStateOf(1f) }
    var showToolPicker by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }
    var showWidthPicker by remember { mutableStateOf(false) }

    // Drawing canvas with pan/zoom
    val canvasModifier = Modifier
        .fillMaxSize()
        .background(Color(backgroundColor))
        .pointerInput(Unit) {
            var currentStroke: MutableList<StrokePoint> = mutableListOf()
            var isDrawing = false

            detectDragGestures(
                onDragStart = { offset ->
                    if (selectedTool != ToolType.PAN && selectedTool != ToolType.SELECT) {
                        isDrawing = true
                        currentStroke = mutableListOf()
                        val localOffset = (offset - panOffset) / zoom
                        currentStroke.add(StrokePoint(
                            x = localOffset.x,
                            y = localOffset.y,
                            pressure = 1.0f
                        ))
                    }
                },
                onDrag = { change, offset ->
                    if (isDrawing && selectedTool != ToolType.PAN && selectedTool != ToolType.SELECT) {
                        val localOffset = (change.position - panOffset) / zoom
                        // Apply palm rejection - ignore touches with very low pressure or large contact area
                        if (palmRejection && (change.pressure < 0.1f || change.size > 100f)) {
                            return@detectDragGestures
                        }
                        currentStroke.add(StrokePoint(
                            x = localOffset.x,
                            y = localOffset.y,
                            pressure = change.pressure,
                            timestamp = System.currentTimeMillis()
                        ))
                    }
                },
                onDragEnd = {
                    if (isDrawing && currentStroke.size >= 2) {
                        val interpolatedStroke = interpolateStroke(currentStroke)
                        val stroke = Stroke(
                            points = interpolatedStroke,
                            color = if (selectedTool == ToolType.ERASER) Color(backgroundColor).toArgb() else strokeColor,
                            strokeWidth = if (selectedTool == ToolType.MARKER) strokeWidth * 2 else strokeWidth,
                            toolType = selectedTool.id,
                            pressureSensitive = selectedTool == ToolType.PEN || selectedTool == ToolType.MARKER
                        )
                        viewModel.addStroke(stroke)
                    }
                    isDrawing = false
                    currentStroke.clear()
                },
                onDragCancel = {
                    isDrawing = false
                    currentStroke.clear()
                }
            )
        }
        .graphicsLayer {
            translationX = panOffset.x
            translationY = panOffset.y
            scaleX = zoom
            scaleY = zoom
        }
        .pointerInput(Unit) {
            detectTransformGestures { centroid, pan, zoomChange, rotation ->
                panOffset = panOffset + pan
                zoom = (zoom * zoomChange).coerceIn(0.5f, 5f)
            }
        }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = note?.title?.ifBlank { "Untitled Note" } ?: "Untitled Note",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackPress) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { viewModel.undo() }, enabled = canUndo) {
                    Icon(Icons.Default.Undo, contentDescription = "Undo")
                }
                IconButton(onClick = { viewModel.redo() }, enabled = canRedo) {
                    Icon(Icons.Default.Redo, contentDescription = "Redo")
                }
                IconButton(onClick = { showToolPicker = true }) {
                    Icon(Icons.Default.Brush, contentDescription = "Tools")
                }
                IconButton(onClick = { showColorPicker = true }) {
                    Icon(Icons.Default.FormatColorFill, contentDescription = "Colors")
                }
                IconButton(onClick = { showWidthPicker = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Stroke Width")
                }
                IconButton(onClick = onExportPng) {
                    Icon(Icons.Default.Download, contentDescription = "Export PNG")
                }
                IconButton(onClick = onShare) {
                    Icon(Icons.Default.Share, contentDescription = "Share")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        )

        // Canvas area
        Canvas(
            modifier = canvasModifier,
            onDrawBehind = {
                // Draw existing strokes with current pan/zoom
                drawStrokes(strokes, panOffset, zoom)
            }
        )

        // Page indicator
        if (pages.size > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.previousPage() }, enabled = currentPageIndex > 0) {
                    Icon(Icons.Default.NavigateBefore, contentDescription = "Previous page")
                }
                Text(
                    text = stringResource(R.string.page_number, currentPageIndex + 1, pages.size),
                    style = MaterialTheme.typography.bodyMedium
                )
                IconButton(onClick = { viewModel.nextPage() }, enabled = currentPageIndex < pages.size - 1) {
                    Icon(Icons.Default.NavigateNext, contentDescription = "Next page")
                }
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { viewModel.addPage() }) {
                    Icon(Icons.Default.Add, contentDescription = "Add page")
                }
            }
        }

        // Bottom Tool Bar
        BottomAppBar(
            modifier = Modifier.padding(bottom = 0.dp),
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Tool buttons
                ToolButton(
                    tool = ToolType.PEN,
                    selected = selectedTool == ToolType.PEN,
                    onClick = { viewModel.setSelectedTool(ToolType.PEN) }
                )
                ToolButton(
                    tool = ToolType.MARKER,
                    selected = selectedTool == ToolType.MARKER,
                    onClick = { viewModel.setSelectedTool(ToolType.MARKER) }
                )
                ToolButton(
                    tool = ToolType.ERASER,
                    selected = selectedTool == ToolType.ERASER,
                    onClick = { viewModel.setSelectedTool(ToolType.ERASER) }
                )
                ToolButton(
                    tool = ToolType.SELECT,
                    selected = selectedTool == ToolType.SELECT,
                    onClick = { viewModel.setSelectedTool(ToolType.SELECT) }
                )
                ToolButton(
                    tool = ToolType.TEXT,
                    selected = selectedTool == ToolType.TEXT,
                    onClick = { viewModel.setSelectedTool(ToolType.TEXT) }
                )

                androidx.compose.foundation.layout.Spacer(modifier = Modifier.weight(1f))

                // Pan/Zoom indicator
                Text(
                    text = "${(zoom * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Reset zoom
                IconButton(onClick = {
                    panOffset = Offset.Zero
                    zoom = 1f
                }) {
                    Icon(Icons.Default.PanTool, contentDescription = "Reset view")
                }
            }
        }

        // Tool Picker Modal
        if (showToolPicker) {
            ToolPickerModal(
                selectedTool = selectedTool,
                onToolSelect = { tool ->
                    viewModel.setSelectedTool(tool)
                    showToolPicker = false
                },
                onDismiss = { showToolPicker = false }
            )
        }

        // Color Picker Modal
        if (showColorPicker) {
            ColorPickerModal(
                selectedColor = strokeColor,
                onColorSelect = { color ->
                    viewModel.setStrokeColor(color)
                    showColorPicker = false
                },
                onDismiss = { showColorPicker = false }
            )
        }

        // Width Picker Modal
        if (showWidthPicker) {
            WidthPickerModal(
                selectedWidth = strokeWidth,
                onWidthSelect = { width ->
                    viewModel.setStrokeWidth(width)
                    showWidthPicker = false
                },
                onDismiss = { showWidthPicker = false }
            )
        }
    }
}

@Composable
fun drawStrokes(strokes: List<Stroke>, panOffset: Offset, zoom: Float) {
    strokes.forEach { stroke ->
        val path = Path()
        val points = stroke.points

        if (points.size < 2) return@forEach

        // Create smooth bezier path
        path.moveTo(points[0].x, points[0].y)

        for (i in 1 until points.size - 1) {
            val current = points[i]
            val next = points[i + 1]
            val controlX = (current.x + next.x) / 2
            val controlY = (current.y + next.y) / 2
            path.quadTo(current.x, current.y, controlX, controlY)
        }

        // Last point
        if (points.size > 1) {
            path.lineTo(points.last().x, points.last().y)
        }

        // Draw with transform applied via canvas
        val color = Color(stroke.color).copy(alpha = if (stroke.toolType == "marker") 0.5f else 1.0f)

        drawPath(
            path = path,
            color = color,
            style = androidx.compose.ui.graphics.Paint.Style.Stroke,
            strokeWidth = stroke.strokeWidth / zoom,
            strokeCap = StrokeCap.Round,
            strokeJoin = StrokeJoin.Round
        )
    }
}

@Composable
fun ToolButton(
    tool: ToolType,
    selected: Boolean,
    onClick: () -> Unit
) {
    val icon = when (tool) {
        ToolType.PEN -> Icons.Default.Edit
        ToolType.MARKER -> Icons.Default.Brush
        ToolType.ERASER -> Icons.Default.Clear
        ToolType.SELECT -> Icons.Default.PanTool
        ToolType.TEXT -> Icons.Default.FormatColorFill
    }

    IconButton(
        onClick = onClick,
        colors = androidx.compose.material3.IconButtonDefaults.iconButtonColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer else androidx.compose.ui.graphics.Color.Transparent,
            contentColor = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Icon(icon, contentDescription = tool.label)
    }
}

@Composable
fun ToolPickerModal(
    selectedTool: ToolType,
    onToolSelect: (ToolType) -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .onClick(onDismiss),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.Card(
            modifier = Modifier
                .width(280.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Select Tool",
                    style = MaterialTheme.typography.titleMedium
                )
                ToolType.values().forEach { tool ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .onClick { onToolSelect(tool) }
                            .background(
                                if (tool == selectedTool) MaterialTheme.colorScheme.primaryContainer else androidx.compose.ui.graphics.Color.Transparent,
                                RoundedCornerShape(8.dp)
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ToolButton(tool, tool == selectedTool, {})
                            androidx.compose.foundation.layout.Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = tool.label,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        if (tool == selectedTool) {
                            Icon(Icons.Default.Check, contentDescription = "Selected", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ColorPickerModal(
    selectedColor: Int,
    onColorSelect: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = listOf(
        0xFF000000, 0xFF6750A4, 0xFF006874, 0xFF625B71,
        0xFFBA1A1A, 0xFFC62828, 0xFFE65100, 0xFFF57F17,
        0xFFF9A825, 0xFF827717, 0xFF33691E, 0xFF1B5E20,
        0xFF004D40, 0xFF006064, 0xFF01579B, 0xFF0D47A1,
        0xFF1A237E, 0xFF311B92, 0xFF4A148C, 0xFF880E4F,
        0xFF3E2723, 0xFF212121
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .onClick(onDismiss),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.Card(
            modifier = Modifier
                .width(320.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Select Color",
                    style = MaterialTheme.typography.titleMedium
                )

                androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                    columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(8),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(colors) { color ->
                        val isSelected = selectedColor == color
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(color), RoundedCornerShape(8.dp))
                                .border(
                                    if (isSelected) 3.dp else 0.dp,
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(if (isSelected) 0.dp else 2.dp)
                                .onClick { onColorSelect(color) },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = "Selected", tint = if (isLightColor(color)) Color.Black else Color.White, modifier = Modifier.size(24.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WidthPickerModal(
    selectedWidth: Float,
    onWidthSelect: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    var width by remember { mutableStateOf(selectedWidth) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .onClick(onDismiss),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.Card(
            modifier = Modifier
                .width(320.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Stroke Width",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "${width.toInt()} dp",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                androidx.compose.material3.Slider(
                    value = width,
                    onValueChange = { width = it },
                    valueRange = 1f..20f,
                    steps = 19,
                    colors = androidx.compose.material3.SliderDefaults.colors(
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        thumbColor = MaterialTheme.colorScheme.primary
                    )
                )

                androidx.compose.material3.Button(
                    onClick = { onWidthSelect(width); onDismiss() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Apply")
                }
            }
        }
    }
}

private fun interpolateStroke(points: List<StrokePoint>): List<StrokePoint> {
    if (points.size < 3) return points

    val interpolated = mutableListOf<StrokePoint>()
    interpolated.add(points[0])

    for (i in 1 until points.size - 1) {
        val prev = points[i - 1]
        val current = points[i]
        val next = points[i + 1]

        // Catmull-Rom spline for smooth interpolation
        val tension = 0.5f
        val x = current.x
        val y = current.y

        // Add interpolated points between current and next
        val steps = 3
        for (step in 1..steps) {
            val t = step / (steps + 1).toFloat()
            val nextX = next.x
            val nextY = next.y

            val interpX = x + (nextX - x) * t
            val interpY = y + (nextY - y) * t
            val interpPressure = current.pressure + (next.pressure - current.pressure) * t

            interpolated.add(StrokePoint(interpX, interpY, interpPressure))
        }
    }

    interpolated.add(points.last())
    return interpolated
}

private fun isLightColor(color: Int): Boolean {
    val r = ((color shr 16) and 0xFF) / 255f
    val g = ((color shr 8) and 0xFF) / 255f
    val b = (color and 0xFF) / 255f
    val luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b
    return luminance > 0.5
}