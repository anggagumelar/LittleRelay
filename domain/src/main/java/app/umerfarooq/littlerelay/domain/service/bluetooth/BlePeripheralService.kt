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

import app.umerfarooq.littlerelay.domain.model.BleAdvertiseSetting
import app.umerfarooq.littlerelay.domain.model.BleDevice
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

sealed interface BlePeripheralServiceState {
    data object Running : BlePeripheralServiceState
    data object Stopped : BlePeripheralServiceState
}
sealed interface GattServerState {
    object Open : GattServerState
    object Close : GattServerState
    data class Error(val exception: Exception) : GattServerState
}

sealed interface AdvertiseState {
    data class Started(val bleAdvertiseSetting: BleAdvertiseSetting) : AdvertiseState
    object Stopped : AdvertiseState
    data class Error(val exception: Exception) : AdvertiseState
}

interface BlePeripheralService {

    val gattServerState: StateFlow<GattServerState>
    val advertiseState: StateFlow<AdvertiseState>
    val serviceState: SharedFlow<BlePeripheralServiceState>
    val connectedDevices: StateFlow<Set<BleDevice>>
    val incomingData: SharedFlow<ByteArray>

    fun startService()
    fun stopService()
    fun advertiseService()
    fun stopAdvertising()

    fun openGatServer()
    fun closeGatServer()
    fun disconnectFromDevice(device: BleDevice)

    fun sendToConnectedDevices(data: ByteArray)
}

class BluetoothAdvertisementFailedException(message: String): Exception(message)