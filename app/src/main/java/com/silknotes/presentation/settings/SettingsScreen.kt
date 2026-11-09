package com.silknotes.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.ArrowBack
import androidx.compose.material3.icons.filled.BrightnessAuto
import androidx.compose.material3.icons.filled.BrightnessHigh
import androidx.compose.material3.icons.filled.BrightnessLow
import androidx.compose.material3.icons.filled.Brush
import androidx.compose.material3.icons.filled.ColorLens
import androidx.compose.material3.icons.filled.Edit
import androidx.compose.material3.icons.filled.FormatListBulleted
import androidx.compose.material3.icons.filled.GridView
import androidx.compose.material3.icons.filled.List
import androidx.compose.material3.icons.filled.Palette
import androidx.compose.material3.icons.filled.TouchApp
import androidx.compose.material3.icons.filled.ViewInAr
import androidx.lifecycle.viewmodel.compose.viewModel
import com.silknotes.R
import com.silknotes.domain.model.SortOrder
import com.silknotes.domain.model.ThemeMode
import com.silknotes.domain.model.ViewMode
import com.silknotes.presentation.theme.SilkNotesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBackPress: () -> Unit) {
    val viewModel: SettingsViewModel = viewModel()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val gridView by viewModel.gridView.collectAsStateWithLifecycle()
    val sortOrder by viewModel.sortOrder.collectAsStateWithLifecycle()
    val showThumbnails by viewModel.showThumbnails.collectAsStateWithLifecycle()
    val defaultStrokeWidth by viewModel.defaultStrokeWidth.collectAsStateWithLifecycle()
    val defaultStrokeColor by viewModel.defaultStrokeColor.collectAsStateWithLifecycle()
    val defaultTool by viewModel.defaultTool.collectAsStateWithLifecycle()
    val pressureSensitivity by viewModel.pressureSensitivity.collectAsStateWithLifecycle()
    val palmRejection by viewModel.palmRejection.collectAsStateWithLifecycle()
    val onboardingComplete by viewModel.onboardingComplete.collectAsStateWithLifecycle()
    val autoSaveInterval by viewModel.autoSaveInterval.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(stringResource(R.string.settings)) },
            navigationIcon = {
                androidx.compose.material3.IconButton(onClick = onBackPress) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        )

        androidx.compose.foundation.layout.Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Appearance Section
                SettingsSection(title = stringResource(R.string.appearance)) {
                    SettingItem(
                        icon = Icons.Default.BrightnessAuto,
                        title = stringResource(R.string.theme),
                        subtitle = when (themeMode) {
                            ThemeMode.SYSTEM.id -> stringResource(R.string.theme_system)
                            ThemeMode.LIGHT.id -> stringResource(R.string.theme_light)
                            ThemeMode.DARK.id -> stringResource(R.string.theme_dark)
                            else -> stringResource(R.string.theme_system)
                        },
                        onClick = { /* Show theme picker dialog */ }
                    )

                    Divider()

                    SettingItem(
                        icon = Icons.Default.GridView,
                        title = stringResource(R.string.default_view),
                        subtitle = if (gridView == ViewMode.GRID.id) stringResource(R.string.view_grid) else stringResource(R.string.view_list),
                        onClick = { /* Show view picker dialog */ }
                    )

                    Divider()

                    SettingItem(
                        icon = Icons.Default.FormatListBulleted,
                        title = stringResource(R.string.sort_order),
                        subtitle = when (sortOrder) {
                            SortOrder.DATE_DESC.id -> stringResource(R.string.sort_date_newest)
                            SortOrder.DATE_ASC.id -> stringResource(R.string.sort_date_oldest)
                            SortOrder.TITLE_ASC.id -> stringResource(R.string.sort_title_az)
                            SortOrder.TITLE_DESC.id -> stringResource(R.string.sort_title_za)
                            else -> stringResource(R.string.sort_date_newest)
                        },
                        onClick = { /* Show sort picker dialog */ }
                    )

                    Divider()

                    SettingItem(
                        icon = Icons.Default.ViewInAr,
                        title = stringResource(R.string.show_thumbnails),
                        subtitle = "",
                        trailing = {
                            Switch(
                                checked = showThumbnails,
                                onCheckedChange = { viewModel.setShowThumbnails(it) },
                                colors = androidx.compose.material3.SwitchDefaults.colors(
                                    thumbColor = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    )
                }

                // Drawing Section
                SettingsSection(title = stringResource(R.string.drawing)) {
                    SettingItem(
                        icon = Icons.Default.Edit,
                        title = stringResource(R.string.default_tool),
                        subtitle = defaultTool.capitalize(),
                        onClick = { /* Show tool picker dialog */ }
                    )

                    Divider()

                    SettingItem(
                        icon = Icons.Default.Brush,
                        title = stringResource(R.string.default_stroke_width),
                        subtitle = "${defaultStrokeWidth} dp",
                        onClick = { /* Show width picker dialog */ }
                    )

                    Divider()

                    SettingItem(
                        icon = Icons.Default.ColorLens,
                        title = stringResource(R.string.default_stroke_color),
                        subtitle = "",
                        trailing = {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color(defaultStrokeColor), RoundedCornerShape(6.dp))
                                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(6.dp))
                            )
                        },
                        onClick = { /* Show color picker dialog */ }
                    )

                    Divider()

                    SettingItem(
                        icon = Icons.Default.TouchApp,
                        title = stringResource(R.string.pressure_sensitivity),
                        subtitle = "${pressureSensitivity}%",
                        trailing = {
                            androidx.compose.foundation.layout.Box(
                                modifier = Modifier.width(120.dp)
                            ) {
                                Slider(
                                    value = pressureSensitivity.toFloat(),
                                    onValueChange = { viewModel.setPressureSensitivity(it.roundToInt()) },
                                    valueRange = 0f..100f,
                                    steps = 10,
                                    colors = androidx.compose.material3.SliderDefaults.colors(
                                        activeTrackColor = MaterialTheme.colorScheme.primary,
                                        inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer,
                                        thumbColor = MaterialTheme.colorScheme.primary
                                    )
                                )
                            }
                        }
                    )

                    Divider()

                    SettingItem(
                        icon = Icons.Default.Palette,
                        title = stringResource(R.string.palm_rejection),
                        subtitle = "",
                        trailing = {
                            Switch(
                                checked = palmRejection,
                                onCheckedChange = { viewModel.setPalmRejection(it) },
                                colors = androidx.compose.material3.SwitchDefaults.colors(
                                    thumbColor = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
                    )
                }

                // About Section
                SettingsSection(title = stringResource(R.string.about)) {
                    SettingItem(
                        icon = Icons.Default.Info,
                        title = stringResource(R.string.app_name),
                        subtitle = stringResource(R.string.version),
                        onClick = { /* Show about dialog */ }
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun SettingItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp)
        .background(
            if (onClick != null) MaterialTheme.colorScheme.surfaceContainerHighest else androidx.compose.ui.graphics.Color.Transparent,
            RoundedCornerShape(8.dp)
        )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .onClick(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp).padding(end = 16.dp)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        trailing?.invoke()
    }
}