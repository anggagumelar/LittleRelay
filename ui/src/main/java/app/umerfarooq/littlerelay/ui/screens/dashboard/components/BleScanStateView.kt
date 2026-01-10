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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.umerfarooq.littlerelay.domain.service.bluetooth.BleScanState
import app.umerfarooq.littlerelay.ui.theme.LittleRelayTheme

@Composable
fun BleScanStateView(
    state: BleScanState,
    modifier: Modifier = Modifier,
    onStartClick: (() -> Unit)? = null,
    onStopClick: (() -> Unit)? = null
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
                is BleScanState.Started -> {
                    StateRow(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        title = "Scanning",
                        description = "Scanning for BLE devices"
                    )

                }

                is BleScanState.Stopped -> {
                    StateRow(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        title = "Not Scanning",
                        description = "Not scanning any device"
                    )
                }
            }

            if(state is BleScanState.Started){
                LinearProgressIndicator(
                    modifier = modifier.align(Alignment.CenterHorizontally)
                )
            }

            // ---- Action Buttons ----
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextButton(
                    onClick = { onStartClick?.invoke() },
                    enabled = state is BleScanState.Stopped,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Start")
                }

                TextButton(
                    onClick = { onStopClick?.invoke() },
                    enabled = state is BleScanState.Started,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Stop")
                }
            }
        }
    }
}


// Preview examples
@Preview(showBackground = true)
@Composable
private fun BleScanStateViewPreviewScanning() {
    LittleRelayTheme {
        BleScanStateView(state = BleScanState.Started)
    }
}

@Preview(showBackground = true)
@Composable
private fun BleScanStateViewPreviewStopped() {
    LittleRelayTheme() {
        BleScanStateView(state = BleScanState.Stopped)
    }
}