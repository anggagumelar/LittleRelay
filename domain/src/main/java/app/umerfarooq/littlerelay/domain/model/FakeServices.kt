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
package app.umerfarooq.littlerelay.domain.model

private val descriptors = listOf(
    // Standard BLE descriptor (Client Characteristic Configuration)
    BleDescriptor(
        uuid = "00002902-0000-1000-8000-00805f9b34fb",
        characteristicUuid = "00002a37-0000-1000-8000-00805f9b34fb"
    ),

    // Unknown / vendor-specific descriptor
    BleDescriptor(
        uuid = "f000aa65-0451-4000-b000-000000000000",
        characteristicUuid = "f000aa61-0451-4000-b000-000000000000"
    ),

    // Another unknown / random descriptor
    BleDescriptor(
        uuid = "9a3f1c20-6d8a-4b2e-9f4a-1a2b3c4d5e6f",
        characteristicUuid = "12345678-1234-5678-1234-56789abcdef0"
    )
)


val fakeServices = listOf(
    BleService(
        uuid = "0000180d-0000-1000-8000-00805f9b34fb",
        isPrimary = true,
        characteristics = listOf(
            BleCharacteristic(
                uuid = "00002a37-0000-1000-8000-00805f9b34fb",
                serviceUuid = "0000180d-0000-1000-8000-00805f9b34fb",
                properties = BleCharacteristicProperties(
                    read = false,
                    write = false,
                    writeNoResponse = false,
                    notify = true,
                    indicate = false,
                    signedWrite = false
                ),
                descriptors = descriptors
            ),
            BleCharacteristic(
                uuid = "00002a38-0000-1000-8000-00805f9b34fb",
                serviceUuid = "0000180d-0000-1000-8000-00805f9b34fb",
                properties = BleCharacteristicProperties(
                    read = true,
                    write = false,
                    writeNoResponse = false,
                    notify = false,
                    indicate = false,
                    signedWrite = false
                ),
                descriptors = descriptors
            )
        )
    ),
    BleService(
        uuid = "0000180f-0000-1000-8000-00805f9b34fb",
        isPrimary = true,
        characteristics = listOf(
            BleCharacteristic(
                uuid = "00002a19-0000-1000-8000-00805f9b34fb",
                serviceUuid = "0000180f-0000-1000-8000-00805f9b34fb",
                properties = BleCharacteristicProperties(
                    read = true,
                    write = false,
                    writeNoResponse = false,
                    notify = true,
                    indicate = false,
                    signedWrite = false
                ),
                descriptors = descriptors
            )
        )
    ),
    BleService(
        uuid = "0000180a-0000-1000-8000-00805f9b34fb",
        isPrimary = false,
        characteristics = listOf(
            BleCharacteristic(
                uuid = "00002a29-0000-1000-8000-00805f9b34fb",
                serviceUuid = "0000180a-0000-1000-8000-00805f9b34fb",
                properties = BleCharacteristicProperties(
                    read = true,
                    write = false,
                    writeNoResponse = false,
                    notify = false,
                    indicate = false,
                    signedWrite = false
                ),
                descriptors = descriptors
            )
        )
    ),
    BleService(
        uuid = "00001816-0000-1000-8000-00805f9b34fb",
        isPrimary = true,
        characteristics = listOf(
            BleCharacteristic(
                uuid = "00002a5b-0000-1000-8000-00805f9b34fb",
                serviceUuid = "00001816-0000-1000-8000-00805f9b34fb",
                properties = BleCharacteristicProperties(
                    read = true,
                    write = true,
                    writeNoResponse = true,
                    notify = false,
                    indicate = true,
                    signedWrite = false
                ),
                descriptors = descriptors
            )
        )
    )
)
