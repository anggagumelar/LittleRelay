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
package app.umerfarooq.littlerelay.data.service.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import app.umerfarooq.littlerelay.domain.model.BleDevice

/**
 * This wrapper class encapsulates the UI-facing state, the low-level hardware reference,
 * and the active communication bridge for a specific remote uiModel.
 *
 * @property domainModel A high-level data model containing uiModel metadata (name, address)
 * suitable for consumption by the UI layer.
 * @property bluetoothDevice A direct reference to the [BluetoothDevice] hardware object,
 * used to initiate GATT connections
 * @property gatt The active [BluetoothGatt] link. This is the "pipe" used to perform
 * read, write, and notification operations. It remains null until a connection is established.
 */
data class RemoteDevice(
    val domainModel: BleDevice,
    val bluetoothDevice: BluetoothDevice,
    val gatt: BluetoothGatt? = null,
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false
)
