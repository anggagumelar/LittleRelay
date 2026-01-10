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
package app.umerfarooq.littlerelay.ui.screens.deviceservices.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.umerfarooq.littlerelay.domain.model.BleCharacteristic
import app.umerfarooq.littlerelay.domain.model.BleService
import app.umerfarooq.littlerelay.domain.model.fakeServices
import app.umerfarooq.littlerelay.ui.theme.LittleRelayTheme

@Composable
fun ServiceItem(
    service: BleService,
    onNotifiableCharacteristicSelectionStateChange: ((BleCharacteristic,Boolean) -> Unit)? = null,
    onWriteableCharacteristicSelectionStateChange: ((BleCharacteristic, Boolean) -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(12.dp)
        ) {
            Text(
                text = service.name(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = service.uuid,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = if (expanded)
                        Icons.Default.ExpandLess
                    else
                        Icons.Default.ExpandMore,
                    contentDescription = null
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))

                service.characteristics.forEach { characteristic ->
                    CharacteristicItem(
                        characteristic = characteristic,
                        onNotifiableCharacteristicSelectionStateChange = onNotifiableCharacteristicSelectionStateChange,
                        onWriteableCharacteristicSelectionStateChange = onWriteableCharacteristicSelectionStateChange
                    )
                }
            }
        }
    }
}

private fun BleService.name(): String {
    return when (uuid.lowercase()) {
        "0000180d-0000-1000-8000-00805f9b34fb" -> "Heart Rate"
        "0000180f-0000-1000-8000-00805f9b34fb" -> "Battery Service"
        "00001816-0000-1000-8000-00805f9b34fb" -> "Cycling Speed"
        "0000180a-0000-1000-8000-00805f9b34fb" -> "Device Information"
        else -> "Unknown Service"
    }
}

@Preview(showBackground = true)
@Composable
private fun ServiceItemPreview() {
    LittleRelayTheme {
        ServiceItem(
            service = fakeServices[0],
        )
    }
}