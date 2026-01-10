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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.umerfarooq.littlerelay.domain.model.BleDevice
import app.umerfarooq.littlerelay.domain.repository.SettingRepository
import app.umerfarooq.littlerelay.domain.service.BoundedServiceInstanceProvider
import app.umerfarooq.littlerelay.domain.service.bluetooth.AdvertiseState
import app.umerfarooq.littlerelay.domain.service.bluetooth.BleCentralService
import app.umerfarooq.littlerelay.domain.service.bluetooth.BleCentralServiceState
import app.umerfarooq.littlerelay.domain.service.bluetooth.BlePeripheralService
import app.umerfarooq.littlerelay.domain.service.bluetooth.BlePeripheralServiceState
import app.umerfarooq.littlerelay.domain.service.bluetooth.BleScanState
import app.umerfarooq.littlerelay.domain.service.bluetooth.GattServerState
import app.umerfarooq.littlerelay.domain.service.bridge.BleToMqttBridgeService
import app.umerfarooq.littlerelay.domain.service.mqtt.MqttService
import app.umerfarooq.littlerelay.domain.util.BluetoothUtil
import app.umerfarooq.littlerelay.domain.util.PermissionsUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val boundedServiceInstanceProvider: BoundedServiceInstanceProvider,
    private val settingRepository: SettingRepository,
    private val permissionsUtil: PermissionsUtil,
    private val bluetoothUtil: BluetoothUtil
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardState())
    val uiState = _uiState.asStateFlow()


    private var mqttService: MqttService? = null
    private var blePeripheralService: BlePeripheralService? = null
    private var bleCentralService: BleCentralService? = null
    private var bleToMqttBridgeService: BleToMqttBridgeService? = null



    init {

        collectMqttServiceStates()
        collectBlePeripheralServiceStates()
        collectBleCentralServiceStates()
        collectBle2MqttBridgeServiceStates()

        viewModelScope.launch {
            settingRepository.bleConfig.collect { bleConfig ->
                _uiState.update {
                    it.copy(
                        bleRole = bleConfig.bleRole
                    )
                }
            }
        }

        viewModelScope.launch {
            bluetoothUtil.bluetoothState.collect { bluetoothState ->
                _uiState.update {
                    it.copy(
                        bluetoothState = bluetoothState
                    )
                }
            }
        }


    }

    fun onEvent(event: DashboardEvent){
        when(event){
           is DashboardEvent.OnCloseGATTServerClick -> {
               blePeripheralService?.closeGatServer()
           }
           is DashboardEvent.OnMqttConnectClick -> {
               mqttService?.connectToBroker()
           }
           is DashboardEvent.OnMqttDisconnectClick -> {
               mqttService?.disconnect()
           }
           is DashboardEvent.OnOpenGATTServerClick -> {
               blePeripheralService?.openGatServer()
           }
           is DashboardEvent.OnStartAdvertisingClick -> {
               blePeripheralService?.advertiseService()
           }
           is DashboardEvent.OnStartBlePeripheralServiceClick -> {
               blePeripheralService?.startService()
           }
           is DashboardEvent.OnStartMQTTServiceClick -> {
               mqttService?.startService()
           }
           is DashboardEvent.OnStopAdvertisingClick -> {
               blePeripheralService?.stopAdvertising()
           }
           is DashboardEvent.OnStopBlePeripheralServiceClick -> {
               blePeripheralService?.stopService()
           }
           is DashboardEvent.OnStopMQTTServiceClick -> {
               mqttService?.stopService()
           }
            is DashboardEvent.OnConnectClick -> {
                // Only Central can initiate connection not Peripheral
                bleCentralService?.connectToDevice(event.bleDevice)
            }
            is DashboardEvent.OnDisconnectClick -> {
                // Once connected, Central or Peripheral either can disconnect from remote device
                bleCentralService?.disconnectFromDevice(event.bleDevice)
                blePeripheralService?.disconnectFromDevice(event.bleDevice)
            }
            DashboardEvent.OnStartBleCentralServiceClick -> {
                bleCentralService?.startService()
            }
            DashboardEvent.OnStartScanClick -> {
                bleCentralService?.scanDevices()
            }
            DashboardEvent.OnStopBleCentralServiceClick -> {
                bleCentralService?.stopService()
            }
            DashboardEvent.OnStopScanClick -> {
                bleCentralService?.stopScan()
            }

            else -> {}


        }

    }

    fun onPermissionsStateChange() {
        _uiState.update {
            it.copy(
                areBluetoothPermissionsGranted = permissionsUtil.areBluetoothPermissionsGranted(),
                locationPermissionGranted = permissionsUtil.isLocationPermissionGranted()
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun collectMqttServiceStates(){
        viewModelScope.launch {
            viewModelScope.launch {
                boundedServiceInstanceProvider.mqttService
                    .filterNotNull()
                    .flatMapLatest { mqttService ->
                        this@DashboardViewModel.mqttService = mqttService

                        combine(
                            mqttService.connectionState,
                            mqttService.serviceState
                        ) { mqttConnectionState, mqttServiceState ->
                            Pair(mqttConnectionState, mqttServiceState)
                        }
                    }
                    .collect { (connectionState, mqttServiceState) ->
                        _uiState.update {
                            it.copy(
                                mqttConnectionState = connectionState,
                                mqttServiceState = mqttServiceState
                            )
                        }
                    }
            }

        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun collectBlePeripheralServiceStates(){

        data class CombinedBlePeripheralState(
            val gattServerState: GattServerState,
            val advertiseState: AdvertiseState,
            val blePeripheralServiceState: BlePeripheralServiceState,
            val connectedBleDevices: Set<BleDevice>
        )

        viewModelScope.launch {
            boundedServiceInstanceProvider.blePeripheralService
                .filterNotNull()
                .flatMapLatest { blePeripheralService ->
                    this@DashboardViewModel.blePeripheralService = blePeripheralService

                    combine(
                        blePeripheralService.gattServerState,
                        blePeripheralService.advertiseState,
                        blePeripheralService.serviceState,
                        blePeripheralService.connectedDevices
                    ){
                        gattServerState, advertiseState, blePeripheralServiceState, connectedDevices ->

                        CombinedBlePeripheralState(
                            gattServerState = gattServerState,
                            advertiseState = advertiseState,
                            blePeripheralServiceState = blePeripheralServiceState,
                            connectedBleDevices = connectedDevices
                        )
                    }
                }.collect { combinedBlePeripheralState ->
                    _uiState.update {
                        it.copy(
                            gattServerState = combinedBlePeripheralState.gattServerState,
                            advertiseState = combinedBlePeripheralState.advertiseState,
                            blePeripheralServiceState = combinedBlePeripheralState.blePeripheralServiceState,
                            connectedBleDevices = combinedBlePeripheralState.connectedBleDevices
                        )
                    }
                }

        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun collectBle2MqttBridgeServiceStates(){


        viewModelScope.launch {
            boundedServiceInstanceProvider.bleToMqttBridgeService
                .filterNotNull()
                .flatMapLatest { bleToMqttBridgeService ->
                    this@DashboardViewModel.bleToMqttBridgeService = bleToMqttBridgeService

                    bleToMqttBridgeService.serviceState

                }.collect { bleToMqttBridgeServiceState ->
                    _uiState.update {
                        it.copy(
                            bleToMqttBridgeServiceState = bleToMqttBridgeServiceState
                        )
                    }
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun collectBleCentralServiceStates(){

        data class CombinedBleCentralState(
            val bleCentralServiceState: BleCentralServiceState,
            val scanState: BleScanState,
            val connectedBleDevices: Set<BleDevice>,
            val scannedBleDevices: Set<BleDevice>,
        )

        viewModelScope.launch {
            boundedServiceInstanceProvider.bleCentralService
                .filterNotNull()
                .flatMapLatest { bleCentralService ->
                    this@DashboardViewModel.bleCentralService = bleCentralService

                    combine(
                        bleCentralService.serviceState,
                        bleCentralService.scanState,
                        bleCentralService.connectedDevices,
                        bleCentralService.scannedDevices
                    ){
                        bleCentralServiceState, scanState, connectedDevices, scannedDevices ->

                        CombinedBleCentralState(
                            bleCentralServiceState = bleCentralServiceState,
                            scanState = scanState,
                            connectedBleDevices = connectedDevices,
                            scannedBleDevices = scannedDevices
                        )

                    }
                }.collect { combinedBleCentralState ->
                    _uiState.update {
                        it.copy(
                            bleCentralServiceState = combinedBleCentralState.bleCentralServiceState,
                            scanState = combinedBleCentralState.scanState,
                            connectedBleDevices = combinedBleCentralState.connectedBleDevices,
                            scannedBleDevices = combinedBleCentralState.scannedBleDevices
                        )
                    }
                }
        }

    }

    override fun onCleared() {
        super.onCleared()
        bluetoothUtil.cleanUp()
    }

}