package com.example.emstrainer.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.emstrainer.model.EMStrainerDataBase
import com.example.emstrainer.model.devices.DevicesRepository
import com.example.emstrainer.model.devices.Devices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeviceViewModel(
    application: Application
) : AndroidViewModel(application) {

    private var deviceRepository: DevicesRepository
    private var listDevices: LiveData<List<Devices>>

    init {
        val deviceDao = EMStrainerDataBase.get(application).devicesDao()
        deviceRepository = DevicesRepository(application)
        listDevices = deviceRepository.list
    }

    fun insertDevice(device: Devices) {
        deviceRepository.insert(device)
    }

    fun deleteDevice(device: Devices) {
        deviceRepository.delete(device)
    }

    fun getAllDevices(): LiveData<List<Devices>> {
        listDevices = deviceRepository.getAllMyProfiles()
        return listDevices
    }
}