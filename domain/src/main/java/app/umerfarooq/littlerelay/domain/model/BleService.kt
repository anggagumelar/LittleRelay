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

data class BleService(
    val uuid: String,
    val isPrimary: Boolean = true,
    val characteristics: List<BleCharacteristic> = emptyList()
)

data class BleCharacteristic(
    val uuid: String,
    val serviceUuid: String,
    val properties: BleCharacteristicProperties,
    val descriptors: List<BleDescriptor> = emptyList(),
    val selected: Boolean = false
)

data class BleCharacteristicProperties(
    val read: Boolean = false,
    val write: Boolean = false,
    val writeNoResponse: Boolean = false,
    val notify: Boolean = false,
    val indicate: Boolean = false,
    val signedWrite: Boolean = false
)

data class BleDescriptor(
    val uuid: String,
    val characteristicUuid: String
)