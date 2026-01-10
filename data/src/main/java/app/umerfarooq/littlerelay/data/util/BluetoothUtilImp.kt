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
package app.umerfarooq.littlerelay.data.util

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.content.ContextCompat
import app.umerfarooq.littlerelay.domain.util.BluetoothState
import app.umerfarooq.littlerelay.domain.util.BluetoothUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class BluetoothUtilImp @Inject constructor(@param:ApplicationContext private val applicationContext: Context) :
    BluetoothUtil {


    private val bluetoothManager =
        applicationContext.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    private val broadcastMessageReceiverHelper = BroadcastMessageReceiverHelper(applicationContext)

    private val _bluetoothState = MutableStateFlow(
        BluetoothState(
            isEnable = isBluetoothEnabled(),
        )
    )

    override val bluetoothState: StateFlow<BluetoothState>
        get() = _bluetoothState.asStateFlow()

    private fun isBluetoothEnabled() = bluetoothManager.adapter.isEnabled

    init {


        broadcastMessageReceiverHelper.registerEvents(IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))

        broadcastMessageReceiverHelper.setOnMessageReceived { intent ->
            if (BluetoothAdapter.ACTION_STATE_CHANGED == intent.action) {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                when (state) {
                    BluetoothAdapter.STATE_OFF -> {
                        Log.d("Bluetooth", "Bluetooth is OFF")
                        _bluetoothState.value = BluetoothState(
                            isEnable = false
                        )
                    }

                    BluetoothAdapter.STATE_ON -> {
                        Log.d("Bluetooth", "Bluetooth is ON")
                        _bluetoothState.value = BluetoothState(
                            isEnable = true
                        )
                    }

                }
            }
        }
    }

    override fun cleanUp() {
        broadcastMessageReceiverHelper.unregisterEvents()
    }

}

private class BroadcastMessageReceiverHelper(private val context: Context) : BroadcastReceiver() {
    private var messageReceiveCallBack: ((Intent) -> Unit)? = null
    private var isRegistered = false

    companion object {
        private val TAG: String = BroadcastMessageReceiverHelper::class.java.simpleName
    }

    fun setOnMessageReceived(callBack: ((Intent) -> Unit)?) {
        messageReceiveCallBack = callBack
    }

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d(TAG, "onReceive() intent = [$intent]")
        if (intent != null) {
            messageReceiveCallBack?.invoke(intent)
        }

    }

    fun registerEvents(intentFilter: IntentFilter) {
        Log.d(TAG, "registerEvents() called")

        try {
            if (!isRegistered)
                ContextCompat.registerReceiver(
                    context,
                    this,
                    intentFilter,
                    ContextCompat.RECEIVER_NOT_EXPORTED
                )


            isRegistered = true
        } catch (e: IllegalArgumentException) {
            isRegistered = false
            e.printStackTrace()
        }
    }

    fun unregisterEvents() {
        Log.d(TAG, "unregister() called")

        try {
            if (isRegistered)
                context.unregisterReceiver(this)

            isRegistered = false
        } catch (e: IllegalArgumentException) {
            isRegistered = false
            e.printStackTrace()
        }
    }

}