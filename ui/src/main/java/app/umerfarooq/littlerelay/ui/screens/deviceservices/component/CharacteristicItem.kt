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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.umerfarooq.littlerelay.domain.model.BleCharacteristic
import app.umerfarooq.littlerelay.domain.model.BleCharacteristicProperties
import app.umerfarooq.littlerelay.domain.model.fakeServices
import app.umerfarooq.littlerelay.ui.theme.LittleRelayTheme


@Composable
fun CharacteristicItem(
    characteristic: BleCharacteristic,
    onNotifiableCharacteristicSelectionStateChange: ((BleCharacteristic,Boolean) -> Unit)? = null,
    onWriteableCharacteristicSelectionStateChange: ((BleCharacteristic, Boolean) -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, top = 8.dp)
    ) {
        Text(
            text = "Characteristic",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        Text(
            text = characteristic.name(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
        )

        Text(
            text = characteristic.uuid,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            CharacteristicPropertiesRow(properties = characteristic.properties)

            if (characteristic.descriptors.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))

                characteristic.descriptors.forEach { descriptor ->
                    DescriptorItem(descriptor = descriptor)
                }

                Spacer(modifier = Modifier.height(6.dp))
            }

            if(characteristic.properties.notify || characteristic.properties.indicate){
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier.padding(5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "Supports notifications/indications. When enabled, updates from this characteristic are forwarded to the broker.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    ListItem(
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        ),
                        headlineContent = {Text("Enable")},
                        supportingContent = {
                            Text("Forward notifications/indications to broker")
                        },
                        trailingContent = {
                            Switch(
                                checked = characteristic.selected,
                                onCheckedChange = {
                                    onNotifiableCharacteristicSelectionStateChange?.invoke(characteristic, it)
                                }
                            )
                        }
                    )
                }
            }

            if(characteristic.properties.writeNoResponse){
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier.padding(5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "Supports writing. Enable this to let the app write incoming broker data to this characteristic",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    ListItem(
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        ),
                        headlineContent = {Text("Enable")},
                        supportingContent = {
                            Text("Write to this characteristic when data is received from broker")
                        },
                        trailingContent = {
                            Switch(
                                checked = characteristic.selected,
                                onCheckedChange = {
                                    onWriteableCharacteristicSelectionStateChange?.invoke(characteristic, it)
                                }
                            )
                        }
                    )
                }
            }
        }

    }
}


@Composable
private fun CharacteristicPropertiesRow(
    properties: BleCharacteristicProperties
) {
    val props = buildList {
        if (properties.read) add("READ")
        if (properties.write) add("WRITE")
        if (properties.writeNoResponse) add("WRITE NO RESPONSE")
        if (properties.notify) add("NOTIFY")
        if (properties.indicate) add("INDICATE")
        if (properties.signedWrite) add("SIGNED")
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(Modifier.height(10.dp))
        Text(
            text = "Properties",
            style = MaterialTheme.typography.labelLarge
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            props.forEach { prop ->
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            text = prop,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )
            }
        }
    }
}


private fun BleCharacteristic.name(): String {
    return when (uuid.lowercase()) {
        "00002a37-0000-1000-8000-00805f9b34fb" -> "Heart Rate Measurement"
        "00002a38-0000-1000-8000-00805f9b34fb" -> "Body Sensor Location"
        "00002a19-0000-1000-8000-00805f9b34fb" -> "Battery Level"
        "00002a5b-0000-1000-8000-00805f9b34fb" -> "CSC Measurement"
        "00002a29-0000-1000-8000-00805f9b34fb" -> "Manufacturer Name"
        else -> "Unknown Characteristic"
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    LittleRelayTheme {
        CharacteristicItem(
            characteristic = fakeServices[0].characteristics[0].copy(
                properties = fakeServices[0].characteristics[0].properties.copy(
                    writeNoResponse = true
                ),
                descriptors = listOf(
                    fakeServices[0].characteristics[0].descriptors[0]
                )
            )
        )
    }
}