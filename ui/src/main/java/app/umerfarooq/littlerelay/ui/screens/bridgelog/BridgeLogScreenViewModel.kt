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
package app.umerfarooq.littlerelay.ui.screens.bridgelog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.umerfarooq.littlerelay.domain.service.BoundedServiceInstanceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BridgeLogScreenViewModel @Inject constructor(
    private val boundedServiceInstanceProvider: BoundedServiceInstanceProvider
) : ViewModel() {


    private val _state = MutableStateFlow(BridgeLogScreenState())
    val state = _state.asStateFlow()

    init {

        viewModelScope.launch {
            boundedServiceInstanceProvider.bleToMqttBridgeService
                .filterNotNull()
                .flatMapLatest { bridgeService ->
                    bridgeService.logs
                }.collect { logs ->
                    _state.update {
                        it.copy(
                            bridgeLogs = logs
                        )
                    }
                }
        }

    }

    fun onEvent(event: BridgeLogScreenEvent) {
        when (event) {
            BridgeLogScreenEvent.OnClearLogsClick -> {
                _state.update { it.copy(bridgeLogs = emptyList()) }
            }
            BridgeLogScreenEvent.OnAutoScrollClick -> {
                _state.update { it.copy(autoScroll = !it.autoScroll) }
            }
            else -> {}
        }
    }

}