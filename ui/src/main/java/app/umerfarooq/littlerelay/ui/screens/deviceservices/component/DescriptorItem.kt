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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.umerfarooq.littlerelay.domain.model.BleDescriptor
import app.umerfarooq.littlerelay.ui.theme.LittleRelayTheme

@Composable
fun DescriptorItem(
    descriptor: BleDescriptor
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, top = 4.dp),
    ) {
        Text(
            text = "Descriptor:",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.tertiary
        )

        Text(
            text = descriptor.name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.tertiary
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = descriptor.uuid,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    LittleRelayTheme{
        DescriptorItem(
            descriptor = BleDescriptor(
                uuid = "00002a37-0000-1000-8000-00805f9b34fb",
                characteristicUuid = "00002a37-0000-1000-8000-00805f9b34fb"
            )
        )
    }
}


private val BleDescriptor.name: String
    get() = knownBleDescriptors[uuid.lowercase()] ?: "unknown"

private val knownBleDescriptors = mapOf(
    // Standard GATT Descriptors
    "00002900-0000-1000-8000-00805f9b34fb" to "Characteristic Extended Properties",
    "00002901-0000-1000-8000-00805f9b34fb" to "Characteristic User Description",
    "00002902-0000-1000-8000-00805f9b34fb" to "Client Characteristic Configuration",
    "00002903-0000-1000-8000-00805f9b34fb" to "Server Characteristic Configuration",
    "00002904-0000-1000-8000-00805f9b34fb" to "Characteristic Presentation Format",
    "00002905-0000-1000-8000-00805f9b34fb" to "Characteristic Aggregate Format"
)

