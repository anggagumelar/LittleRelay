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
package app.umerfarooq.littlerelay.data.mapper

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import app.umerfarooq.littlerelay.domain.model.BleCharacteristic
import app.umerfarooq.littlerelay.domain.model.BleCharacteristicProperties
import app.umerfarooq.littlerelay.domain.model.BleDescriptor
import app.umerfarooq.littlerelay.domain.model.BleService

fun BluetoothGattService.toDomain(): BleService {
    return BleService(
        uuid = this.uuid.toString(),
        isPrimary = this.type == BluetoothGattService.SERVICE_TYPE_PRIMARY,
        characteristics = this.characteristics.toDomain()
    )
}

fun BluetoothGattCharacteristic.toDomain(): BleCharacteristic {
    return BleCharacteristic(
        uuid = this.uuid.toString(),
        serviceUuid = this.service.uuid.toString(),
        properties = BleCharacteristicProperties(
            read = properties and BluetoothGattCharacteristic.PROPERTY_READ != 0,
            write = properties and BluetoothGattCharacteristic.PROPERTY_WRITE != 0,
            writeNoResponse = properties and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE != 0,
            notify = (properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0),
            indicate = (properties and BluetoothGattCharacteristic.PROPERTY_INDICATE != 0),
            signedWrite = (properties and BluetoothGattCharacteristic.PROPERTY_SIGNED_WRITE != 0),
        ),
        descriptors = this.descriptors.toDomain(),
    )

}

fun BluetoothGattDescriptor.toDomain(): BleDescriptor {
    return BleDescriptor(
        uuid = this.uuid.toString(),
        characteristicUuid = this.characteristic.uuid.toString()
    )
}
@JvmName("characteristicsToDomain")
fun List<BluetoothGattCharacteristic>.toDomain(): List<BleCharacteristic> {
    return this.map { it.toDomain() }
}
@JvmName("descriptorsToDomain")
fun List<BluetoothGattDescriptor>.toDomain(): List<BleDescriptor> {
    return this.map { it.toDomain() }
}
@JvmName("servicesToDomain")
fun List<BluetoothGattService>.toDomain(): List<BleService> {
    return this.map { it.toDomain() }
}





