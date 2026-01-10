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
package app.umerfarooq.littlerelay.ui.screens.bleconfig

import app.umerfarooq.littlerelay.domain.model.AdvertiseMode
import app.umerfarooq.littlerelay.domain.model.AdvertiseTxPower
import app.umerfarooq.littlerelay.domain.model.BleRole

sealed interface BleConfigScreenEvent {
    data class OnBleRoleChange(val bleRole: BleRole) : BleConfigScreenEvent
    data class OnTxPowerLevelChange(val txPowerLevel: AdvertiseTxPower) : BleConfigScreenEvent
    data class OnAdvertiseModeChange(val mode: AdvertiseMode) : BleConfigScreenEvent
    data class OnIncludeDeviceNameChange(val includeDeviceName: Boolean) : BleConfigScreenEvent
    data object OnBackClick : BleConfigScreenEvent
}


