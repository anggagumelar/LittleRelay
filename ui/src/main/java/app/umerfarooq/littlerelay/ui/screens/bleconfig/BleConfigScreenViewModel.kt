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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.umerfarooq.littlerelay.domain.repository.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BleConfigScreenViewModel @Inject constructor(
    private val settingRepository: SettingRepository
) : ViewModel(){

    val _state = MutableStateFlow(BleConfigScreenState())
    val state = _state.asStateFlow()


    init {
        viewModelScope.launch {
            settingRepository.bleConfig.collect { bleConfig ->
                _state.update {
                    it.copy(bleConfig = bleConfig)
                }
            }
        }
    }


    fun onEvent(event: BleConfigScreenEvent){
        when(event){
            is BleConfigScreenEvent.OnBleRoleChange -> {
                viewModelScope.launch {
                    settingRepository.updateBleConfig { oldBleConfig ->
                        oldBleConfig.copy(
                            bleRole = event.bleRole
                        )
                    }
                }
            }

            is BleConfigScreenEvent.OnAdvertiseModeChange -> {
                viewModelScope.launch {
                    settingRepository.updateBleConfig { oldBleConfig ->
                        oldBleConfig.copy(
                            bleAdvertiseSetting = oldBleConfig.bleAdvertiseSetting.copy(
                                mode = event.mode
                            )
                        )
                    }
                }
            }
            is BleConfigScreenEvent.OnIncludeDeviceNameChange -> {
                viewModelScope.launch {
                    settingRepository.updateBleConfig { oldBleConfig ->
                        oldBleConfig.copy(
                            bleAdvertiseSetting = oldBleConfig.bleAdvertiseSetting.copy(
                                includeDeviceName = event.includeDeviceName
                            )
                        )
                    }
                }
            }
            is BleConfigScreenEvent.OnTxPowerLevelChange -> {
                viewModelScope.launch {
                    settingRepository.updateBleConfig { oldBleConfig ->
                        oldBleConfig.copy(
                            bleAdvertiseSetting = oldBleConfig.bleAdvertiseSetting.copy(
                                txPowerLevel = event.txPowerLevel
                            )
                        )
                    }
                }
            }

            else -> {}
        }

    }

}