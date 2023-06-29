package com.example.emstrainer.model.devices

import androidx.room.*

@Dao
interface DevicesDao {

    /*
    @Insert
    suspend fun insertDevice(devices: Devices)

    @Delete
    suspend fun deleteDevice(devices: Devices)

    @Query("select * from devices")
    fun allDevices(): List<Devices>
    */

    @Insert
    fun insertDevice(devices: Devices)

    @Delete
    fun deleteDevice(devices: Devices)

    @Update
    fun updateDevice(devices: Devices)

    @Query("select * from devices")
    fun allDevices(): List<Devices>

}