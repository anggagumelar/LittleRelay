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
package app.umerfarooq.littlerelay.data.service.mqtt

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import app.umerfarooq.littlerelay.domain.repository.SettingRepository
import app.umerfarooq.littlerelay.domain.service.mqtt.MqttConnectionState
import app.umerfarooq.littlerelay.domain.service.mqtt.MqttService
import app.umerfarooq.littlerelay.domain.service.mqtt.MqttServiceState
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MqttServiceImp : Service(), MqttService {

    @Inject
    lateinit var settingRepository: SettingRepository

    private val _connectionState = MutableStateFlow<MqttConnectionState>(MqttConnectionState.Disconnected)
    override val connectionState = _connectionState.asStateFlow()

    private val _serviceState = MutableStateFlow<MqttServiceState>(MqttServiceState.Stopped)
    override val serviceState = _serviceState.asStateFlow()

    private val _incomingData = MutableSharedFlow<ByteArray>()
    override val incomingData = _incomingData.asSharedFlow()

    private var mqtt5BlockingClient: Mqtt5BlockingClient? = null

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val notificationManager: NotificationManager by lazy{ getSystemService(NotificationManager::class.java) as NotificationManager }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate()")
        createNotificationChannel()

        scope.launch {
            connectionState.collect { connectionState ->
                if(serviceState.value is MqttServiceState.Running)
                    setNotificationContent(connectionState.toString())
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(
            TAG,
            "onStartCommand() called with: intent = [$intent], flags = [$flags], startId = [$startId]"
        )

        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                .apply {
                    setSmallIcon(R.drawable.stat_notify_sync)
                    setContentTitle("Mqtt Service")
                    setContentText("Running")
                    setPriority(NotificationCompat.PRIORITY_HIGH)
                    setAutoCancel(false)
                    setOngoing(true)
                }


        val notification = notificationBuilder.build()

        startForeground(ON_GOING_NOTIFICATION_ID, notification)


        setServiceState(MqttServiceState.Running)

        return START_NOT_STICKY
    }


    private fun connect() {

        if (mqtt5BlockingClient?.state?.isConnected == true)
            return

        scope.launch {
            setConnectionState(MqttConnectionState.Connecting)

            val mqttConfig = settingRepository.mqttConfig.first()

            mqtt5BlockingClient = MqttClient.builder()
                .serverHost(mqttConfig.brokerAddress)
                .serverPort(mqttConfig.brokerPort)
                .identifier(UUID.randomUUID().toString())
                .useMqttVersion5()
                .let { mqtt5ClientBuilder ->

                    if(mqttConfig.useSSL)
                        return@let mqtt5ClientBuilder.sslWithDefaultConfig()

                    mqtt5ClientBuilder
                }
                .let { mqtt5ClientBuilder ->

                    if(mqttConfig.useCredentials){
                        return@let mqtt5ClientBuilder.simpleAuth()
                            .username(mqttConfig.userName)
                            .password(mqttConfig.password.toByteArray())
                            .applySimpleAuth()
                    }                    

                    mqtt5ClientBuilder
                }
                .transportConfig()
                .mqttConnectTimeout(mqttConfig.connectionTimeoutSecs.toLong(), TimeUnit.SECONDS)
                .applyTransportConfig()
                .addConnectedListener { context ->
                    Log.d(TAG, context.toString())
                    setConnectionState(MqttConnectionState.Connected)
                }
                .addDisconnectedListener { context ->
                    Log.d(TAG, context.toString())
                    setConnectionState(MqttConnectionState.Disconnected)
                }
                .buildBlocking()

            mqttConfig.topicFilters.forEach { topicFilter ->
                mqtt5BlockingClient?.toAsync()?.subscribeWith()
                    ?.topicFilter(topicFilter.topic)
                    ?.qos(when(topicFilter.qos){
                        0 -> MqttQos.AT_MOST_ONCE
                        1 -> MqttQos.AT_LEAST_ONCE
                        2 -> MqttQos.EXACTLY_ONCE
                        else -> MqttQos.AT_MOST_ONCE
                    })
                    ?.callback { message ->
                        scope.launch {
                            _incomingData.emit(message.payloadAsBytes)
                        }
                    }
                    ?.send()
            }

            try {

                mqtt5BlockingClient?.connect()

            } catch (e: Exception) {
                e.printStackTrace()
                setConnectionState(MqttConnectionState.ConnectionError(e))

            }

        }
    }

    private fun setNotificationContent(message: String){

        notificationManager.notify(
            ON_GOING_NOTIFICATION_ID,
            NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                .apply {
                    setSmallIcon(R.drawable.stat_notify_sync)
                    setContentTitle(NOTIFICATION_TITLE)
                    setContentText(message)
                    setPriority(NotificationCompat.PRIORITY_HIGH)
                    setAutoCancel(false)
                    setOngoing(true)
                }.build()
        )
    }


    override fun startService() {
        ContextCompat.startForegroundService(
            applicationContext,
            Intent(applicationContext, MqttServiceImp::class.java)
        )
    }

    override fun stopService() {
        disconnect()
        stopForeground()
        stopSelf()
        setServiceState(MqttServiceState.Stopped)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun connectToBroker() {
       connect()
    }

    override fun disconnect() {
        scope.launch {
            if (mqtt5BlockingClient?.state?.isConnected == true)
                mqtt5BlockingClient?.disconnect()
        }
    }

    override fun sendToBroker(data: ByteArray) {
        scope.launch {
            val mqttConfig = settingRepository.mqttConfig.first()

            mqttConfig.topicPublish.forEach { topicPublish ->
                try {
                    mqtt5BlockingClient?.publish(
                        Mqtt5Publish.builder()
                            .topic(topicPublish.topic)
                            .payload(data)
                            .qos(when(topicPublish.qos){
                                0 -> MqttQos.AT_MOST_ONCE
                                1 -> MqttQos.AT_LEAST_ONCE
                                2 -> MqttQos.EXACTLY_ONCE
                                else -> MqttQos.AT_MOST_ONCE
                            })
                            .build()
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setConnectionState(mqttConnectionState: MqttConnectionState){
        _connectionState.value = mqttConnectionState
    }

    private fun setServiceState(mqttServiceState: MqttServiceState){
        _serviceState.value = mqttServiceState
    }


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "createNotificationChannel()")
            val name: CharSequence = "Little Relay"
            val description = "Notifications from Little Relay MQTTS Service"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    @Suppress("DEPRECATION")
    private fun stopForeground() {
        /*
        If the device is running an older version of Android,
        we fallback to stopForeground(true) to remove the service from the foreground and dismiss the ongoing notification.
        Although it shows as deprecated, it should still work as expected on API level 21 (Android 5).
         */

        // for Android 7 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            stopForeground(STOP_FOREGROUND_REMOVE)
        else
        // This method was deprecated in API level 33.
        // Ignore deprecation message as there is no other alternative method for Android 6 and lower
            stopForeground(true)
    }

    // Binder given to clients
    private val binder: IBinder = LocalBinder()

    override fun onBind(intent: Intent) = binder

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {


        // Return this instance of LocalService so clients can call public methods
        val service: MqttServiceImp
            get() = this@MqttServiceImp // Return this instance of LocalService so clients can call public methods

    }

    companion object {
        private val TAG: String = MqttServiceImp::class.java.getSimpleName()
        const val NOTIFICATION_CHANNEL_ID = "Little Relay Mqtt Notification"

        // cannot be zero
        const val ON_GOING_NOTIFICATION_ID = 833
        const val NOTIFICATION_TITLE = "MQTT Service"
    }

}