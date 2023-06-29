package com.example.emstrainer.presenter

import android.bluetooth.BluetoothSocket
import android.util.Log
import com.example.emstrainer.R
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ConnectedDriverThread(
    private val mmSocket: BluetoothSocket
) : Thread() {
    private var saveStates = SaveStates
    private var mmInStream: InputStream
    private var mmOutStream: OutputStream
    private val connectedDriverTAG = "ConnectedThread"

    @ExperimentalUnsignedTypes
    override fun run() {
        val buffer1 = ByteArray(1)           // буферное хранилище для потока
        val buffer2 = ByteArray(1)

        while(true) {
            try { // Чтение из входного потока
                mmInStream.read(buffer1)         // Получаем кол-во байт и само собщение в байтовый массив "buffer"
                mmInStream.read(buffer2)
                val ubuffer = buffer1.toUByteArray() + buffer2.toUByteArray()
                savingLOG(ubuffer)
                settingNewValues(ubuffer)
            } catch(e: IOException) {
                Log.d(connectedDriverTAG, "!!!!ConnectedThread.run() - IOException - RUN!!!!")
                saveStates.connectStatus = false
                break
            }
        }
    }

    @ExperimentalUnsignedTypes
    private fun settingNewValues(ubuffer: UByteArray) {
        when(ubuffer[0]){
            207.toUByte() -> saveBatteryLevel(ubuffer[1].toInt())
            241.toUByte() -> saveStates.power1 = ubuffer[1].toInt()
            242.toUByte() -> saveStates.power2 = ubuffer[1].toInt()
            243.toUByte() -> saveStates.power3 = ubuffer[1].toInt()
            244.toUByte() -> saveStates.power4 = ubuffer[1].toInt()
            245.toUByte() -> saveStates.power5 = ubuffer[1].toInt()
            246.toUByte() -> saveStates.power6 = ubuffer[1].toInt()
            247.toUByte() -> saveStates.power7 = ubuffer[1].toInt()
            248.toUByte() -> saveStates.power8 = ubuffer[1].toInt()
            249.toUByte() -> saveStates.power9 = ubuffer[1].toInt()
            250.toUByte() -> saveStates.power10 = ubuffer[1].toInt()
            251.toUByte() -> saveStates.power11 = ubuffer[1].toInt()
            252.toUByte() -> saveStates.power12 = ubuffer[1].toInt()
            253.toUByte() -> analysisModulationFrequency(ubuffer[1])
            225.toUByte() -> saveStates.byte225 = ubuffer[1].toInt()
            208.toUByte() -> saveStates.byte208 = ubuffer[1].toInt()
        }
    }

    @ExperimentalUnsignedTypes
    private fun saveBatteryLevel(batteryLevelByte: Int) {
        Log.d(connectedDriverTAG, "!!!!!!!! saveBatteryLevel!!!!!!! $batteryLevelByte!!!!!!!!")
        saveStates.batteryLevel = (100 - (127 - batteryLevelByte) / 0.31).toInt()
        Log.d(
            connectedDriverTAG,
            "!!!!!!!! saveStates.batteryLevel!!!!!!! $saveStates.batteryLevel!!!!!!!!"
        )
    }

    @ExperimentalUnsignedTypes
    private fun savingLOG(ubuffer: UByteArray) {
        when(ubuffer[0]){
            241.toUByte() -> saveStates.LOG =
                saveStates.LOG + "power1 " + ubuffer[0] + " " + ubuffer[1] + "\n"
            242.toUByte() -> saveStates.LOG =
                saveStates.LOG + "power2 " + ubuffer[0] + " " + ubuffer[1] + "\n"
            243.toUByte() -> saveStates.LOG =
                saveStates.LOG + "power3 " + ubuffer[0] + " " + ubuffer[1] + "\n"
            244.toUByte() -> saveStates.LOG =
                saveStates.LOG + "power4 " + ubuffer[0] + " " + ubuffer[1] + "\n"
            245.toUByte() -> saveStates.LOG =
                saveStates.LOG + "power5 " + ubuffer[0] + " " + ubuffer[1] + "\n"
            246.toUByte() -> saveStates.LOG =
                saveStates.LOG + "power6 " + ubuffer[0] + " " + ubuffer[1] + "\n"
            247.toUByte() -> saveStates.LOG =
                saveStates.LOG + "power7 " + ubuffer[0] + " " + ubuffer[1] + "\n"
            248.toUByte() -> saveStates.LOG =
                saveStates.LOG + "power8 " + ubuffer[0] + " " + ubuffer[1] + "\n"
            249.toUByte() -> saveStates.LOG =
                saveStates.LOG + "power9 " + ubuffer[0] + " " + ubuffer[1] + "\n"
            250.toUByte() -> saveStates.LOG =
                saveStates.LOG + "power10 " + ubuffer[0] + " " + ubuffer[1] + "\n"
            251.toUByte() -> saveStates.LOG =
                saveStates.LOG + "power11 " + ubuffer[0] + " " + ubuffer[1] + "\n"
            252.toUByte() -> saveStates.LOG =
                saveStates.LOG + "power12 " + ubuffer[0] + " " + ubuffer[1] + "\n"
            253.toUByte() -> saveStates.LOG =
                saveStates.LOG + "ModulationFrequency: " + ubuffer[1].toInt() + "\n"
            225.toUByte() -> saveStates.LOG =
                saveStates.LOG + "225 byte " + ubuffer[0] + " " + ubuffer[1] + "\n"
            208.toUByte() -> saveStates.LOG =
                saveStates.LOG + "208 byte " + ubuffer[0] + " " + ubuffer[1] + "\n"
        }

        if(ubuffer[0] == 207.toUByte()) {
            saveStates.LOG = saveStates.LOG + "batteryLevel: " + ubuffer[1].toInt() + "\n"
        }
    }

    @ExperimentalUnsignedTypes
    private fun analysisModulationFrequency(value: UByte) {
        saveStates.switchFm = value and 0b00000001.toUByte() == 0b00000001.toUByte()
        saveStates.switchAm = value and 0b00000010.toUByte() == 0b00000010.toUByte()

        when(value and 0b00111100.toUByte()) {
            0b00000100.toUByte() -> saveStates.radioId = R.id.radioFreq128Frag
            0b00001000.toUByte() -> saveStates.radioId = R.id.radioFreq64Frag
            0b00010000.toUByte() -> saveStates.radioId = R.id.radioFreq32Frag
            0b00100000.toUByte() -> saveStates.radioId = R.id.radioFreq16Frag
        }
    }

    fun allStopPowerCommandDriverFullSuit(message: ByteArray) {
        val byteArray: ByteArray = byteArrayOf(message[0])
        mmOutStream.write(byteArray)
        sleep(50)
    }

    fun write(message: ByteArray) {
        val firstByteArray: ByteArray = byteArrayOf(message[0])
        mmOutStream.write(firstByteArray)
        sleep(50)

        val secondByteArray: ByteArray = byteArrayOf(message[1])
        mmOutStream.write(secondByteArray)
        sleep(50)
    }

    fun cancel() {
        mmSocket.close()
    }

    init {
        lateinit var tmpIn: InputStream
        lateinit var tmpOut: OutputStream

        try {
            tmpIn = mmSocket.inputStream
            tmpOut = mmSocket.outputStream
        } catch(e: IOException) {
        }
        mmInStream = tmpIn
        mmOutStream = tmpOut
    }
}