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


object BLEPeripheralServiceInfo {
    const val SERVICE_UUID = "d2dfc1a2-9f53-494b-a2a1-1efd6148aa81"
    const val NOTIFIABLE_CHARACTERISTIC_UUID = "007a7899-dc76-4778-b33b-3460bf250eed"

    /**
     * The standard Bluetooth Low Energy (BLE) Client Characteristic Configuration Descriptor (CCCD) UUID.
     *
     * This descriptor UUID (`00002902-0000-1000-8000-00805f9b34fb`) is required to enable notifications
     * or indications for a specific characteristic on a peripheral device. Writing to this descriptor
     * tells the peripheral that the client wants to subscribe to updates.
     */
    const val NOTIFIABLE_DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb"
    const val WRITEABLE_CHARACTERISTIC_UUID = "eeb6d18b-863b-4fb5-81b6-a38902b985df"
}