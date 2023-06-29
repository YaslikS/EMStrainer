package com.example.emstrainer.presenter

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.example.emstrainer.presenter.SaveStates.Companion.maxPower
import java.io.IOException
import java.lang.Exception
import java.lang.reflect.Method
import java.util.*

abstract class MainPresenter {
    companion object {
        private var btAdapter = BluetoothAdapter.getDefaultAdapter()
        private var btSocket: BluetoothSocket? = null
        private var device: BluetoothDevice? = null
        private const val mainPresenterTAG = "BGTpresenter"
        private var saveStates = SaveStates
        private const val twoByteMask = 0b00000000
        private var connectedDriverThread: ConnectedDriverThread? = null
        private var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        fun onConnectFullSuit(address: String): Boolean {
            Log.d(mainPresenterTAG, "...onConnect - попытка соединения...")
            device = btAdapter.getRemoteDevice(address)
            //deviceMainPresenter.setPin("1234".toByteArray())
            //deviceMainPresenter.createBond()

            try {
                btSocket = device!!.createRfcommSocketToServiceRecord(m_myUUID)
                Log.d(
                    mainPresenterTAG,
                    "btSocket = device.createRfcommSocketToServiceRecord(UUID.randomUUID())"
                )
            } catch(e: IOException) {
                Log.d(
                    mainPresenterTAG,
                    "...onConnect - IOException - btSocket = device.createRfcommSocketToServiceRecord(m_myUUID)"
                )
            }
            btAdapter.cancelDiscovery()
            Log.d(mainPresenterTAG, "bluetoothAdapter.cancelDiscovery()")
            Log.d(mainPresenterTAG, "...Connecting...")
            try {
                btSocket?.connect()
                Log.d(mainPresenterTAG, "...The connection is established and ready for data transfer...")
                Log.d(mainPresenterTAG, "...Creating Socket...")
                connectedDriverThread = ConnectedDriverThread(btSocket!!/*, h*/)
                connectedDriverThread!!.start()
                saveStates.nameSelectDevice = device!!.name
                return true
            } catch(e: IOException) {
                return try {
                    btSocket?.close()
                    Log.d(mainPresenterTAG, "btSocket.close()")
                    false
                } catch(e2: IOException) {
                    Log.d(mainPresenterTAG, "...onConnect - IOException - btSocket.connect()")
                    false
                }
            }
        }

        fun unpairDevice(){
            val pairedDevices: Set<BluetoothDevice> = btAdapter.bondedDevices
            if(pairedDevices.isNotEmpty()) {
                for(device in pairedDevices) {
                    try {
                        if(device.name.contains("EMS OutFit")) {
                            val m: Method =
                                device.javaClass.getMethod("removeBond", null as Class<*>?)
                            m.invoke(device, null as Array<Any?>?)
                        }
                    } catch(e: Exception) {
                        Log.d("fail", e.message!!)
                    }
                }
            }
        }

        fun onBluetooth() {
            if(btAdapter != null) if(!btAdapter.isEnabled) btAdapter.enable()
        }

        fun offConnect() {
            connectedDriverThread?.cancel()
        }

        fun startModulationAndFrequencyFullSuit(thirdByteOptionSettings: Byte) {
            val byteArray: ByteArray =
                byteArrayOf(0b10101101.toByte(), (twoByteMask + thirdByteOptionSettings).toByte())
            connectedDriverThread?.write(byteArray)
        }

        fun allStopPowerCommandPresenterFullSuit() {
            val byteArray = byteArrayOf(0b10111110.toByte())
            connectedDriverThread?.allStopPowerCommandDriverFullSuit(byteArray)
        }

        fun startBroadcastCommandPresenterFullSuit() {
            val byteArray = byteArrayOf(0b10111101.toByte())
            connectedDriverThread?.allStopPowerCommandDriverFullSuit(byteArray)
        }

        fun stopBroadcastCommandPresenterFullSuit() {
            val byteArray = byteArrayOf(0b10111100.toByte())
            connectedDriverThread?.allStopPowerCommandDriverFullSuit(byteArray)
        }

        fun startPowerFullSuit(
            power1: Int,
            power2: Int,
            power3: Int,
            power4: Int,
            power5: Int,
            power6: Int,
            power7: Int,
            power8: Int,
            power9: Int,
            power10: Int,
            power11: Int,
            power12: Int
        ): Boolean {
            if(checkValuePowerFullSuit(
                    power1,
                    power2,
                    power3,
                    power4,
                    power5,
                    power6,
                    power7,
                    power8,
                    power9,
                    power10,
                    power11,
                    power12
                )
            ) {
                val byteArray1: ByteArray =
                    byteArrayOf(0b10010001.toByte(), (twoByteMask + power1).toByte())
                connectedDriverThread?.write(byteArray1)
                val byteArray2: ByteArray =
                    byteArrayOf(0b10010010.toByte(), (twoByteMask + power2).toByte())
                connectedDriverThread?.write(byteArray2)
                val byteArray3: ByteArray =
                    byteArrayOf(0b10010011.toByte(), (twoByteMask + power3).toByte())
                connectedDriverThread?.write(byteArray3)
                val byteArray4: ByteArray =
                    byteArrayOf(0b10010100.toByte(), (twoByteMask + power4).toByte())
                connectedDriverThread?.write(byteArray4)
                val byteArray5: ByteArray =
                    byteArrayOf(0b10010101.toByte(), (twoByteMask + power5).toByte())
                connectedDriverThread?.write(byteArray5)
                val byteArray6: ByteArray =
                    byteArrayOf(0b10010110.toByte(), (twoByteMask + power6).toByte())
                connectedDriverThread?.write(byteArray6)
                val byteArray7: ByteArray =
                    byteArrayOf(0b10010111.toByte(), (twoByteMask + power7).toByte())
                connectedDriverThread?.write(byteArray7)
                val byteArray8: ByteArray =
                    byteArrayOf(0b10011000.toByte(), (twoByteMask + power8).toByte())
                connectedDriverThread?.write(byteArray8)
                val byteArray9: ByteArray =
                    byteArrayOf(0b10011001.toByte(), (twoByteMask + power9).toByte())
                connectedDriverThread?.write(byteArray9)
                val byteArray10: ByteArray =
                    byteArrayOf(0b10011010.toByte(), (twoByteMask + power10).toByte())
                connectedDriverThread?.write(byteArray10)
                val byteArray11: ByteArray =
                    byteArrayOf(0b10011011.toByte(), (twoByteMask + power11).toByte())
                connectedDriverThread?.write(byteArray11)
                val byteArray12: ByteArray =
                    byteArrayOf(0b10011100.toByte(), (twoByteMask + power12).toByte())
                connectedDriverThread?.write(byteArray12)

                return true
            } else {
                return false
            }
        }

        private fun checkValuePowerFullSuit(
            power1: Int,
            power2: Int,
            power3: Int,
            power4: Int,
            power5: Int,
            power6: Int,
            power7: Int,
            power8: Int,
            power9: Int,
            power10: Int,
            power11: Int,
            power12: Int
        ): Boolean {
            var checkValue: Boolean = true
            if(power1 < 0 || power1 > maxPower) checkValue = false
            if(power2 < 0 || power2 > maxPower) checkValue = false
            if(power3 < 0 || power3 > maxPower) checkValue = false
            if(power4 < 0 || power4 > maxPower) checkValue = false
            if(power5 < 0 || power5 > maxPower) checkValue = false
            if(power6 < 0 || power6 > maxPower) checkValue = false
            if(power7 < 0 || power7 > maxPower) checkValue = false
            if(power8 < 0 || power8 > maxPower) checkValue = false
            if(power9 < 0 || power9 > maxPower) checkValue = false
            if(power10 < 0 || power10 > maxPower) checkValue = false
            if(power11 < 0 || power11 > maxPower) checkValue = false
            if(power12 < 0 || power12 > maxPower) checkValue = false
            return checkValue
        }
    }
}