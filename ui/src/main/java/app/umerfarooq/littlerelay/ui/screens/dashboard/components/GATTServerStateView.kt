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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.umerfarooq.littlerelay.domain.service.bluetooth.GattServerState
import app.umerfarooq.littlerelay.ui.theme.LittleRelayTheme

@Composable
fun GattServerStateView(
    state: GattServerState,
    onOpenClick: () -> Unit,
    onCloseClick: () -> Unit,
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
                is GattServerState.Open -> {
                    StateRow(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        title = "Server Open",
                        description = "GATT server is running"
                    )
                }

                is GattServerState.Close -> {
                    StateRow(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        title = "Server Closed",
                        description = "GATT server is not running"
                    )
                }

                is GattServerState.Error -> {
                    StateRow(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        title = "Server Error",
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
                    onClick = onOpenClick,
                    enabled = state.canOpen(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Open")
                }

                TextButton(
                    onClick = onCloseClick,
                    enabled = state.canClose(),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

fun GattServerState.canOpen(): Boolean =
    this is GattServerState.Close || this is GattServerState.Error

fun GattServerState.canClose(): Boolean =
    this is GattServerState.Open

@Preview
@Composable
fun GattServerStatePreview() {
    LittleRelayTheme {
        GattServerStateView(
            state = GattServerState.Open,
            onOpenClick = {},
            onCloseClick = {}
        )
    }
}
