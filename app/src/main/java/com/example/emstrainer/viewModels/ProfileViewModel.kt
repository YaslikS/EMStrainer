package com.example.emstrainer.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.emstrainer.model.EMStrainerDataBase
import com.example.emstrainer.model.profilesFullSuit.ProfileFullSuit
import com.example.emstrainer.model.profilesFullSuit.ProfileFullSuitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private var profileRepository: ProfileFullSuitRepository
    private var listProfiles: LiveData<List<ProfileFullSuit>>

    init {
        val profileDao = EMStrainerDataBase.get(application).profileFullSuitDao()
        profileRepository = ProfileFullSuitRepository(application)
        listProfiles = profileRepository.list
    }

    fun insertProfile(profile: ProfileFullSuit) {
        profileRepository.insert(profile)
    }

    fun deleteProfile(profile: ProfileFullSuit) {
        profileRepository.delete(profile)
    }

    fun getAllProfiles(): LiveData<List<ProfileFullSuit>> {
        listProfiles = profileRepository.getAllMyProfiles()
        return listProfiles
    }

    fun getEasyProfiles(): LiveData<List<ProfileFullSuit>> {
        listProfiles = profileRepository.getAllEasyProfiles()
        return listProfiles
    }

    fun getMediumProfiles(): LiveData<List<ProfileFullSuit>> {
        listProfiles = profileRepository.getAllMediumProfiles()
        return listProfiles
    }

    fun getHardProfiles(): LiveData<List<ProfileFullSuit>> {
        listProfiles = profileRepository.getAllHardProfiles()
        return listProfiles
    }
}
