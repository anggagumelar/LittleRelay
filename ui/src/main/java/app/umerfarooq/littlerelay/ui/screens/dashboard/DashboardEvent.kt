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
package app.umerfarooq.littlerelay.ui.screens.dashboard

import app.umerfarooq.littlerelay.domain.model.BleDevice

sealed interface DashboardEvent {
    data object OnStartMQTTServiceClick: DashboardEvent
    data object OnStopMQTTServiceClick: DashboardEvent
    data object OnMqttConnectClick: DashboardEvent
    data object OnMqttDisconnectClick: DashboardEvent
    data object OnStartBlePeripheralServiceClick: DashboardEvent
    data object OnStopBlePeripheralServiceClick: DashboardEvent
    data object OnStartBleCentralServiceClick: DashboardEvent
    data object OnStopBleCentralServiceClick: DashboardEvent
    data object OnStartScanClick: DashboardEvent
    data object OnStopScanClick: DashboardEvent
    data class OnConnectClick(val bleDevice: BleDevice): DashboardEvent
    data class OnDisconnectClick(val bleDevice: BleDevice): DashboardEvent
    data object OnStartAdvertisingClick: DashboardEvent
    data object OnStopAdvertisingClick: DashboardEvent
    data object OnOpenGATTServerClick: DashboardEvent
    data object OnCloseGATTServerClick: DashboardEvent
    data object OnGrantBluetoothPermissionClick: DashboardEvent
    data object OnGrantLocationPermissionClick: DashboardEvent
    data object OnNavigateToMqttSettingsClick: DashboardEvent
    data object OnNavigateToBLeSettingClick: DashboardEvent
    data class OnViewDeviceServicesClick(val bleDevice: BleDevice): DashboardEvent
    data object OnBridgeLogsClick: DashboardEvent
    data object OnAboutClick: DashboardEvent
}
