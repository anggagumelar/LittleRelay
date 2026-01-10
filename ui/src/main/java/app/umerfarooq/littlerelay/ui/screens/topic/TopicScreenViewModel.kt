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
package app.umerfarooq.littlerelay.ui.screens.topic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.umerfarooq.littlerelay.domain.model.TopicFilter
import app.umerfarooq.littlerelay.domain.model.TopicPublish
import app.umerfarooq.littlerelay.domain.repository.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicScreenViewModel @Inject constructor(
    private val settingRepository: SettingRepository
) : ViewModel() {

    val _state = MutableStateFlow(TopicScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingRepository.mqttConfig.collect { config ->
                _state.update {
                    it.copy(
                        topicFilters = config.topicFilters,
                        topicPublishes = config.topicPublish
                    )
                }
            }
        }
    }

    fun onEvent(event: TopicScreenEvent) {
        when (event) {
            is TopicScreenEvent.OnAddNewTopic -> {
                viewModelScope.launch {
                    settingRepository.updateMqttConfig { oldConfig ->
                        val newConfig = when(event.topic){
                            is TopicFilter -> oldConfig.copy(topicFilters = oldConfig.topicFilters + event.topic)
                            is TopicPublish -> oldConfig.copy(topicPublish = oldConfig.topicPublish + event.topic)
                        }
                        newConfig
                    }
                }
            }
            is TopicScreenEvent.OnDeleteTopicClick -> {
                viewModelScope.launch {
                    settingRepository.updateMqttConfig { oldConfig ->

                        val newConfig = when(event.topic){
                            is TopicFilter -> oldConfig.copy(topicFilters = oldConfig.topicFilters - event.topic)
                            is TopicPublish -> oldConfig.copy(topicPublish = oldConfig.topicPublish - event.topic)
                        }

                        newConfig
                    }
                }
            }

            is TopicScreenEvent.OnUpdateTopicClick -> {
                viewModelScope.launch {
                    settingRepository.updateMqttConfig { oldConfig ->

                        val newConfig = when(event.topic){
                            is TopicFilter -> oldConfig.copy(topicFilters = oldConfig.topicFilters.filter { it.id == event.topic.id }.map { event.topic })
                            is TopicPublish -> oldConfig.copy(topicPublish = oldConfig.topicPublish.filter { it.id == event.topic.id }.map { event.topic })
                        }

                        newConfig
                    }
                }
            }

            else -> {}
        }
    }
}