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
package app.umerfarooq.littlerelay.ui.screens.bridgelog.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.umerfarooq.littlerelay.domain.model.BridgeLog
import app.umerfarooq.littlerelay.domain.model.LogType
import app.umerfarooq.littlerelay.ui.theme.LittleRelayTheme

@Composable
fun BridgeLogItem(
    log: BridgeLog,
    modifier: Modifier = Modifier
) {
    val containerColor = when (log.type) {
        LogType.Info -> MaterialTheme.colorScheme.primaryContainer
        LogType.Warning -> MaterialTheme.colorScheme.errorContainer
    }

    val contentColor = when (log.type) {
        LogType.Info -> MaterialTheme.colorScheme.onPrimaryContainer
        LogType.Warning -> MaterialTheme.colorScheme.onErrorContainer
    }

    val icon = when (log.type) {
        LogType.Info -> Icons.Default.Info
        LogType.Warning -> Icons.Default.Warning
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = log.type.name,
                tint = contentColor,
                modifier = Modifier.size(18.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = log.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor
                )

                Text(
                    text = log.timeFormatted,
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColor.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBridgeLogItem() {
    LittleRelayTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BridgeLogItem(
                log = BridgeLog(
                    message = "Connection established successfully",
                    type = LogType.Info
                )
            )

            BridgeLogItem(
                log = BridgeLog(
                    message = "Network latency is higher than expected",
                    type = LogType.Warning
                )
            )
        }
    }
}