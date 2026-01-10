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
package app.umerfarooq.littlerelay.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.umerfarooq.littlerelay.data.di.IoDispatcher
import app.umerfarooq.littlerelay.domain.model.BleConfig
import app.umerfarooq.littlerelay.domain.model.MqttConfig
import app.umerfarooq.littlerelay.domain.repository.SettingRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

//The delegate will ensure that we have a single instance of DataStore with that name in our application.
private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore("settings")


class SettingRepositoryImp @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SettingRepository {

    private object Key {
        val MQTT_CONFIG_JSON = stringPreferencesKey("mqtt_config_json")
        val BLE_CONFIG_JSON = stringPreferencesKey("ble_config_json")
    }

    override val mqttConfig: Flow<MqttConfig>
        get() = context.userPreferencesDataStore.data.map { pref ->
            pref[Key.MQTT_CONFIG_JSON]?.let { MqttConfig.fromJson(it) } ?: MqttConfig()
        }.flowOn(ioDispatcher)

    override val bleConfig: Flow<BleConfig>
        get() = context.userPreferencesDataStore.data.map { pref ->
            pref[Key.BLE_CONFIG_JSON]?.let { BleConfig.fromJson(it) } ?: BleConfig()
        }.flowOn(ioDispatcher)


    override suspend fun updateMqttConfig(mqttConfig: (settings: MqttConfig) -> MqttConfig) {
        val oldConfig = this.mqttConfig.first()
        val newConfig = mqttConfig.invoke(oldConfig)
        saveMqttConfig(newConfig)
    }

    private suspend fun saveMqttConfig(config: MqttConfig) {
        context.userPreferencesDataStore.edit { pref ->
            pref[Key.MQTT_CONFIG_JSON] = config.toJson()
        }
    }

    override suspend fun updateBleConfig(bleConfig: (config: BleConfig) -> BleConfig) {
        val oldConfig = this.bleConfig.first()
        val newConfig = bleConfig.invoke(oldConfig)
        saveBleConfig(newConfig)
    }

    private suspend fun saveBleConfig(config: BleConfig) {
        context.userPreferencesDataStore.edit { pref ->
            pref[Key.BLE_CONFIG_JSON] = config.toJson()
        }
    }
}


/*
class MqttConfigRepositoryImp @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MqttConfigRepository {

    private object Key {
        val BROKER_ADDRESS = stringPreferencesKey("broker_address")
        val BROKER_PORT = intPreferencesKey("broker_port")
        val USERNAME = stringPreferencesKey("username")
        val PASSWORD = stringPreferencesKey("password")
        val USE_SSL = booleanPreferencesKey("use_ssl")
        val USE_WEBSOCKET = booleanPreferencesKey("use_websocket")
        val USE_CREDENTIALS = booleanPreferencesKey("use_credentials")
        val CONNECTION_TIMEOUT_SECS = intPreferencesKey("connection_timeout_secs")
        val SUBSCRIPTIONS = stringPreferencesKey("subscriptions")
    }

    override val settings: Flow<MqttConfig>
        get() = context.userPreferencesDataStore.data.map { pref ->
            MqttConfig(
                brokerAddress = pref[Key.BROKER_ADDRESS] ?: MqttConfigDefaults.BROKER_ADDRESS,
                brokerPort = pref[Key.BROKER_PORT] ?: MqttConfigDefaults.BROKER_PORT,
                userName = pref[Key.USERNAME] ?: MqttConfigDefaults.USER_NAME,
                password = pref[Key.PASSWORD] ?: MqttConfigDefaults.PASSWORD,
                useSSL = pref[Key.USE_SSL] ?: MqttConfigDefaults.USE_SSL,
                useWebsocket = pref[Key.USE_WEBSOCKET] ?: MqttConfigDefaults.USE_WEBSOCKET,
                useCredentials = pref[Key.USE_CREDENTIALS] ?: MqttConfigDefaults.USE_CREDENTIALS,
                connectionTimeoutSecs = pref[Key.CONNECTION_TIMEOUT_SECS]
                    ?: MqttConfigDefaults.CONNECTION_TIMEOUT_SECS,
            )
        }.flowOn(ioDispatcher)

    override suspend fun updateConfig(mqttConfig: (settings: MqttConfig) -> MqttConfig) {
        val oldSettings = this.settings.first()
        val newSettings = mqttConfig.invoke(oldSettings)
        saveSettings(newSettings)
    }

    private suspend fun saveSettings(config: MqttConfig) {

        context.userPreferencesDataStore.edit { pref ->
            pref[Key.BROKER_ADDRESS] = config.brokerAddress
            pref[Key.BROKER_PORT] = config.brokerPort
            pref[Key.USERNAME] = config.userName
            pref[Key.PASSWORD] = config.password
            pref[Key.USE_SSL] = config.useSSL
            pref[Key.USE_WEBSOCKET] = config.useWebsocket
            pref[Key.USE_CREDENTIALS] = config.useCredentials
            pref[Key.CONNECTION_TIMEOUT_SECS] = config.connectionTimeoutSecs
        }

    }
}
 */