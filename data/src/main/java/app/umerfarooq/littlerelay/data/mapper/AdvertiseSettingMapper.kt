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

import android.bluetooth.le.AdvertiseSettings
import app.umerfarooq.littlerelay.domain.model.AdvertiseMode
import app.umerfarooq.littlerelay.domain.model.AdvertiseTxPower
import app.umerfarooq.littlerelay.domain.model.BleAdvertiseSetting

fun AdvertiseSettings.toDomain() : BleAdvertiseSetting {

    val mode = when(this.mode){
        AdvertiseSettings.ADVERTISE_MODE_LOW_POWER -> AdvertiseMode.LOW_POWER
        AdvertiseSettings.ADVERTISE_MODE_BALANCED -> AdvertiseMode.BALANCED
        AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY -> AdvertiseMode.LOW_LATENCY
        else -> AdvertiseMode.BALANCED
    }

    val txPower = when(this.txPowerLevel){
        AdvertiseSettings.ADVERTISE_TX_POWER_ULTRA_LOW -> AdvertiseTxPower.ULTRA_LOW
        AdvertiseSettings.ADVERTISE_TX_POWER_LOW -> AdvertiseTxPower.LOW
        AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM -> AdvertiseTxPower.MEDIUM
        AdvertiseSettings.ADVERTISE_TX_POWER_HIGH -> AdvertiseTxPower.HIGH
        else -> AdvertiseTxPower.MEDIUM
    }

    return BleAdvertiseSetting(
        isConnectable = isConnectable,
        timeout = timeout,
        mode = mode,
        txPowerLevel = txPower

    )
}