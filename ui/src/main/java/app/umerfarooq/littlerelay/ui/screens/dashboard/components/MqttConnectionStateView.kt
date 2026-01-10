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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.umerfarooq.littlerelay.domain.service.mqtt.MqttConnectionState
import app.umerfarooq.littlerelay.ui.theme.LittleRelayTheme


@Composable
fun MqttConnectionStateView(
    state: MqttConnectionState,
    onConnectClick: () -> Unit,
    onDisconnectClick: () -> Unit,
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
                is MqttConnectionState.Connecting -> {
                    StateRow(
                        icon = {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        },
                        title = "Connecting",
                        description = "Establishing MQTT connection…"
                    )
                }

                is MqttConnectionState.Connected -> {
                    StateRow(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.CloudDone,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        title = "Connected",
                        description = "MQTT connection established"
                    )
                }

                is MqttConnectionState.Disconnected -> {
                    StateRow(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.CloudOff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        title = "Disconnected",
                        description = "Not connected to the broker"
                    )
                }

                is MqttConnectionState.ConnectionTimeout -> {
                    StateRow(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        title = "Connection Timeout",
                        description = "The connection attempt timed out",
                        isError = true
                    )
                }

                is MqttConnectionState.ConnectionError -> {
                    StateRow(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        title = "Connection Error",
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
                    onClick = onConnectClick,
                    enabled = state.canConnect(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Connect")
                }

                TextButton(
                    onClick = onDisconnectClick,
                    enabled = state.canDisconnect(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Disconnect")
                }
            }
        }
    }
}


private fun MqttConnectionState.canConnect(): Boolean =
    this !is MqttConnectionState.Connected &&
            this !is MqttConnectionState.Connecting

private fun MqttConnectionState.canDisconnect(): Boolean =
    this is MqttConnectionState.Connected


@Preview
@Composable
private fun Preview(modifier: Modifier = Modifier) {
    LittleRelayTheme {
        MqttConnectionStateView(
            state = MqttConnectionState.Connected,
            onConnectClick = {},
            onDisconnectClick = {}
        )
    }
}