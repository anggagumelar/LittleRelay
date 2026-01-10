/*
 *     This file is a part of LittleRelay (https://www.github.com/UmerCodez/LittleRelay)
 *     Copyright (C) 2026 Umer Farooq (umerfarooq2383@gmail.com)
 *
 *     LittleRelay is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     LittleRelay is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with LittleRelay. If not, see <https://www.gnu.org/licenses/>.
 *
 */
package app.umerfarooq.littlerelay.ui.screens.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.PublicOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.umerfarooq.littlerelay.domain.model.AdvertiseMode
import app.umerfarooq.littlerelay.domain.model.AdvertiseTxPower
import app.umerfarooq.littlerelay.domain.model.BleAdvertiseSetting
import app.umerfarooq.littlerelay.domain.service.bluetooth.AdvertiseState
import app.umerfarooq.littlerelay.ui.theme.LittleRelayTheme

@Composable
fun AdvertiseStateView(
    state: AdvertiseState,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ---- State UI ----
            when (state) {
                is AdvertiseState.Started -> {
                    StateRow(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        title = "Advertising",
                        description = "BLE advertising is active"
                    )

                    //BleAdvertiseSettingCard(state.bleAdvertiseSetting)
                }

                is AdvertiseState.Stopped -> {
                    StateRow(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.PublicOff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        title = "Not Advertising",
                        description = "BLE advertising is stopped"
                    )
                }

                is AdvertiseState.Error -> {
                    StateRow(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        title = "Advertising Error",
                        description = state.exception.message ?: "An unknown error occurred",
                        isError = true
                    )
                }
            }

            // ---- Action Buttons ----
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextButton(
                    onClick = onStartClick,
                    enabled = state.canStart(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Start")
                }

                TextButton(
                    onClick = onStopClick,
                    enabled = state.canStop(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Stop")
                }
            }
        }
    }
}

private fun AdvertiseState.canStart(): Boolean =
    this is AdvertiseState.Stopped ||
            this is AdvertiseState.Error

private fun AdvertiseState.canStop(): Boolean =
    this is AdvertiseState.Started


@Composable
private fun BleAdvertiseSettingCard(
    setting: BleAdvertiseSetting,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "BLE Advertise Settings",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            HorizontalDivider()

            SettingRow(
                icon = Icons.Default.Info,
                label = "Connectable",
                value = if (setting.isConnectable) "Yes" else "No"
            )

            SettingRow(
                icon = Icons.Default.Info,
                label = "TX Power Level",
                value = setting.txPowerLevel.toString()
            )

            SettingRow(
                icon = Icons.Default.Info,
                label = "Timeout",
                value = "${setting.timeout} ms"
            )

            SettingRow(
                icon = Icons.Default.Info,
                label = "Advertise Mode",
                value = setting.mode.toString()
            )
        }
    }
}

@Composable
private fun SettingRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
private fun AdvertiseStateViewPreview () {
    LittleRelayTheme {
        AdvertiseStateView(
            state = AdvertiseState.Started(
                bleAdvertiseSetting = BleAdvertiseSetting(
                    isConnectable = true,
                    txPowerLevel = AdvertiseTxPower.MEDIUM,
                    timeout = 2,
                    mode = AdvertiseMode.BALANCED
                )
            ),
            onStartClick = {},
            onStopClick = {}
        )
    }
}

@Preview(name = "Advertisement Stop")
@Composable
private fun AdvertiseStateViewPreview2 () {
    LittleRelayTheme {
        AdvertiseStateView(
            state = AdvertiseState.Stopped,
            onStartClick = {},
            onStopClick = {}
        )
    }
}


