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
package app.umerfarooq.littlerelay.domain.service.mqtt

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow


sealed interface MqttServiceState{
    data object Running: MqttServiceState
    data object Stopped: MqttServiceState
}

sealed interface MqttConnectionState{
    data object Connecting: MqttConnectionState
    data object Connected: MqttConnectionState
    data object Disconnected: MqttConnectionState
    data class ConnectionError(val exception: Throwable): MqttConnectionState
    data object ConnectionTimeout: MqttConnectionState
}

interface MqttService{

    val connectionState: StateFlow<MqttConnectionState>
    val serviceState: StateFlow<MqttServiceState>
    val incomingData: SharedFlow<ByteArray>

    fun startService()
    fun stopService()
    fun connectToBroker()
    fun disconnect()
    fun sendToBroker(data: ByteArray)

}