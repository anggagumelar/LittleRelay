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
package app.umerfarooq.littlerelay.ui.screens.mqttconfig

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
class MqttConfigScreenViewModel @Inject constructor(
    private val settingRepository: SettingRepository
) : ViewModel(){

    private val _mqttConfigScreenState = MutableStateFlow(MqttConfigScreenState())
    val mqttConfigScreenState = _mqttConfigScreenState.asStateFlow()

    init {

        viewModelScope.launch {
            settingRepository.mqttConfig.collect { config ->
               _mqttConfigScreenState.update {
                   it.copy(
                       mqttConfig = config
                   )
               }
            }
        }

    }

    fun onEvent(event : MqttConfigScreenEvent){
        when(event){
            is MqttConfigScreenEvent.OnBrokerAddressChange -> {
                viewModelScope.launch {
                    settingRepository.updateMqttConfig { oldConfig ->
                        oldConfig.copy(
                            brokerAddress = event.address
                        )
                    }
                }
            }
            is MqttConfigScreenEvent.OnBrokerPortChange -> {
                viewModelScope.launch {
                    settingRepository.updateMqttConfig { oldConfig ->
                        oldConfig.copy(
                            brokerPort = event.brokerPort
                        )
                    }
                }
            }
            is MqttConfigScreenEvent.OnConnectionTimeoutChange -> {
                viewModelScope.launch {
                    settingRepository.updateMqttConfig { oldConfig ->
                        oldConfig.copy(
                            connectionTimeoutSecs = event.connectionTimeout
                        )
                    }
                }
            }
            is MqttConfigScreenEvent.OnPasswordChange -> {
                viewModelScope.launch {
                    settingRepository.updateMqttConfig { oldConfig ->
                        oldConfig.copy(
                            password = event.password
                        )
                    }
                }
            }
            is MqttConfigScreenEvent.OnUseCredentialsChange -> {
                viewModelScope.launch {
                    settingRepository.updateMqttConfig { oldConfig ->
                        oldConfig.copy(
                            useCredentials = event.useCredentials
                        )
                    }
                }
            }
            is MqttConfigScreenEvent.OnUseSSLChange -> {
                viewModelScope.launch {
                    settingRepository.updateMqttConfig { oldConfig ->
                        oldConfig.copy(
                            useSSL = event.useSSL
                        )
                    }
                }
            }
            is MqttConfigScreenEvent.OnUseWebsocketChange -> {
                // TODO: add this later
            }
            is MqttConfigScreenEvent.OnUserNameChange -> {
                viewModelScope.launch {
                    settingRepository.updateMqttConfig { oldConfig ->
                        oldConfig.copy(
                            userName = event.userName
                        )
                    }
                }
            }

            is MqttConfigScreenEvent.OnBackClick -> {}
            is MqttConfigScreenEvent.OnManageTopicsClick -> {}
        }
    }

}