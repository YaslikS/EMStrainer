package com.example.emstrainer.model.profilesFullSuit

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.emstrainer.model.EMStrainerDataBase.Companion.get

class ProfileFullSuitRepository(
    private val application: Application
) {
    val list = MutableLiveData<List<ProfileFullSuit>>()

    fun insert(profile: ProfileFullSuit) {
        Insert(application, profile).execute()
    }

    fun update(profile: ProfileFullSuit) {
        Update(application, profile).execute()
    }

    fun delete(profile: ProfileFullSuit) {
        Delete(application, profile).execute()
    }

    fun getAllMyProfiles(): MutableLiveData<List<ProfileFullSuit>> {
        list.value = GetMyAllProfiles(application).execute().get()
        return list
    }

    fun getAllEasyProfiles(): MutableLiveData<List<ProfileFullSuit>> {
        list.value = GetMyEasyProfiles(application).execute().get()
        return list
    }

    fun getAllMediumProfiles(): MutableLiveData<List<ProfileFullSuit>> {
        list.value = GetMyMediumProfiles(application).execute().get()
        return list
    }

    fun getAllHardProfiles(): MutableLiveData<List<ProfileFullSuit>> {
        list.value = GetMyHardProfiles(application).execute().get()
        return list
    }

    private class Insert(val application: Application, val profile: ProfileFullSuit) :
        AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            get(application).profileFullSuitDao().insertProfile(profile)
            return null
        }
    }

    private class Update(val application: Application, val profile: ProfileFullSuit) :
        AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            get(application).profileFullSuitDao().updateProfile(profile)
            return null
        }
    }

    private class Delete(val application: Application, val profile: ProfileFullSuit) :
        AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            get(application).profileFullSuitDao().deleteProfile(profile)
            return null
        }
    }

    private class GetMyAllProfiles(val application: Application) :
        AsyncTask<Void, Void, List<ProfileFullSuit>>() {
        override fun doInBackground(vararg params: Void?): List<ProfileFullSuit>? {
            return get(application).profileFullSuitDao().allProfiles()
        }
    }

    private class GetMyEasyProfiles(val application: Application) :
        AsyncTask<Void, Void, List<ProfileFullSuit>>() {
        override fun doInBackground(vararg params: Void?): List<ProfileFullSuit>? {
            return get(application).profileFullSuitDao().easyProfiles()
        }
    }

    private class GetMyMediumProfiles(val application: Application) :
        AsyncTask<Void, Void, List<ProfileFullSuit>>() {
        override fun doInBackground(vararg params: Void?): List<ProfileFullSuit>? {
            return get(application).profileFullSuitDao().mediumProfiles()
        }
    }

    private class GetMyHardProfiles(val application: Application) :
        AsyncTask<Void, Void, List<ProfileFullSuit>>() {
        override fun doInBackground(vararg params: Void?): List<ProfileFullSuit>? {
            return get(application).profileFullSuitDao().hardProfiles()
        }
    }
}