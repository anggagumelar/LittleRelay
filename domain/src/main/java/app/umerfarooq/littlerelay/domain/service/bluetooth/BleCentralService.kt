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
package app.umerfarooq.littlerelay.domain.service.bluetooth

import app.umerfarooq.littlerelay.domain.model.BleCharacteristic
import app.umerfarooq.littlerelay.domain.model.BleDevice
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

sealed interface BleCentralServiceState {
    data object Running : BleCentralServiceState
    data object Stopped : BleCentralServiceState
}

sealed interface BleScanState {
    object Started : BleScanState
    object Stopped : BleScanState
}

interface BleCentralService {

    val serviceState: StateFlow<BleCentralServiceState>
    val scanState: StateFlow<BleScanState>
    val scannedDevices: StateFlow<Set<BleDevice>>
    val connectedDevices: StateFlow<Set<BleDevice>>
    val incomingData: SharedFlow<ByteArray>


    fun startService()
    fun stopService()
    fun scanDevices()
    fun stopScan()
    fun connectToDevice(device: BleDevice)
    fun disconnectFromDevice(device: BleDevice)
    fun enableNotification(device: BleDevice, bleCharacteristic: BleCharacteristic)
    fun disableNotification(device: BleDevice, bleCharacteristic: BleCharacteristic)
    fun selectWriteableCharacteristic(device: BleDevice, bleCharacteristic: BleCharacteristic)
    fun deselectWriteableCharacteristic(device: BleDevice, bleCharacteristic: BleCharacteristic)
    fun sendToConnectedDevices(data: ByteArray)

}