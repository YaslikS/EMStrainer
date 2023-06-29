package com.example.emstrainer.model.devices

/*
import androidx.lifecycle.MutableLiveData

class DeviceRepository(
    private val deviceDao: DevicesDao
){
    val list = MutableLiveData<List<Devices>>()

    suspend fun insert(device: Devices){
        deviceDao.insertDevice(device)
    }

    suspend fun delete(device: Devices) {
        deviceDao.deleteDevice(device)
    }

    fun getAllMyProfiles(): MutableLiveData<List<Devices>> {
        list.value = deviceDao.allDevices()
        return list
    }

}
*/

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.example.emstrainer.model.EMStrainerDataBase.Companion.get

class DevicesRepository(
    private val application: Application
) {
    val list = MutableLiveData<List<Devices>>()

    fun insert(device: Devices) {
        Insert(application, device).execute()
    }

    fun update(device: Devices) {
        Update(application, device).execute()
    }

    fun delete(device: Devices) {
        Delete(application, device).execute()
    }

    fun getAllMyProfiles(): MutableLiveData<List<Devices>> {
        list.value = GetMyAllDevices(application).execute().get()
        return list
    }

    private class Insert(val application: Application, val device: Devices) :
        AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            get(application).devicesDao().insertDevice(device)
            return null
        }
    }

    private class Update(val application: Application, val device: Devices) :
        AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            get(application).devicesDao().updateDevice(device)
            return null
        }
    }

    private class Delete(val application: Application, val device: Devices) :
        AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            get(application).devicesDao().deleteDevice(device)
            return null
        }
    }

    private class GetMyAllDevices(val application: Application) :
        AsyncTask<Void, Void, List<Devices>>() {
        override fun doInBackground(vararg params: Void?): List<Devices>? {
            return get(application).devicesDao().allDevices()
        }
    }
}
