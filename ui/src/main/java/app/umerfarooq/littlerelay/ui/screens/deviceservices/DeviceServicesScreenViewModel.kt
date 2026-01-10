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
package app.umerfarooq.littlerelay.ui.screens.deviceservices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.umerfarooq.littlerelay.domain.model.BleCharacteristic
import app.umerfarooq.littlerelay.domain.model.BleDevice
import app.umerfarooq.littlerelay.domain.service.BoundedServiceInstanceProvider
import app.umerfarooq.littlerelay.domain.service.bluetooth.BleCentralService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceServicesScreenViewModel @Inject constructor(
    private val serviceProvider: BoundedServiceInstanceProvider
) : ViewModel() {

    private val _state = MutableStateFlow(DeviceServicesScreenState())
    val state = _state.asStateFlow()

    private var bleCentralService: BleCentralService? = null
    private var currentDeviceAddress: MutableStateFlow<String?> = MutableStateFlow(null)

    init {
        viewModelScope.launch {
            serviceProvider.bleCentralService
                .filterNotNull()
                .collectLatest { bleCentralService ->
                    this@DeviceServicesScreenViewModel.bleCentralService = bleCentralService
                    currentDeviceAddress
                        .filterNotNull()
                        .collectLatest { currentDeviceAddress ->
                            bleCentralService.connectedDevices.collect { connectedDevices ->
                                val currentDevice = connectedDevices.find { it.address == currentDeviceAddress }
                                _state.update {
                                    it.copy(
                                        currentDevice = currentDevice,
                                        selectedNotifiableCharacteristics =
                                            currentDevice?.getSelectedNotifiableCharacteristics()
                                                ?: emptySet(),
                                        hasNotifiableCharacteristic =
                                            currentDevice?.hasNotifiableCharacteristic() ?: false,
                                        selectedWriteableCharacteristics =
                                            currentDevice?.getSelectedWriteableCharacteristics()
                                                ?: emptySet(),
                                        hasWriteableCharacteristic =
                                            currentDevice?.hasWriteableCharacteristic() ?: false
                                    )
                                }
                            }
                        }
                }
        }


    }

    fun onEvent(event: DeviceServicesScreenEvent) {
        when (event) {
            is DeviceServicesScreenEvent.OnNotifiableCharacteristicSelectionStateChange -> {

                if (event.selected) {
                    bleCentralService?.enableNotification(
                        event.device,
                        event.bleCharacteristic
                    )
                } else {
                    bleCentralService?.disableNotification(
                        event.device,
                        event.bleCharacteristic
                    )
                }


            }

            is DeviceServicesScreenEvent.OnWriteableCharacteristicSelectionStateChange -> {
                if(event.selected){
                    bleCentralService?.selectWriteableCharacteristic(
                        event.device,
                        event.bleCharacteristic
                    )
                }else{
                    bleCentralService?.deselectWriteableCharacteristic(
                        event.device,
                        event.bleCharacteristic
                    )
                }
            }
        }
    }

    fun loadDevice(address: String) {
        currentDeviceAddress.value = address
    }


    private fun BleDevice.getSelectedNotifiableCharacteristics(): Set<BleCharacteristic> {
        return this.services.flatMap { service -> service.characteristics }
            .filter { characteristic ->
                (characteristic.properties.notify || characteristic.properties.indicate) && characteristic.selected
            }
            .toSet()
    }

    private fun BleDevice.getSelectedWriteableCharacteristics(): Set<BleCharacteristic> {
        return this.services.flatMap { service -> service.characteristics }
            .filter { characteristic ->
                characteristic.properties.writeNoResponse && characteristic.selected
            }
            .toSet()
    }

    private fun BleDevice.hasNotifiableCharacteristic(): Boolean {
        return this.services.any { service ->
            service.characteristics.any { characteristic ->
                (characteristic.properties.notify || characteristic.properties.indicate)
            }
        }
    }

    private fun BleDevice.hasWriteableCharacteristic(): Boolean {
        return this.services.any { service ->
            service.characteristics.any { characteristic ->
                (characteristic.properties.writeNoResponse)
            }
        }
    }

}