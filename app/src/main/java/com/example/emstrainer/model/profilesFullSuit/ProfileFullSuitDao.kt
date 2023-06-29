package com.example.emstrainer.model.profilesFullSuit

import androidx.room.*

@Dao
interface ProfileFullSuitDao {

    /*
    @Insert
    suspend fun insertProfile(profile: ProfileFullSuit)
    @Delete
    suspend fun deleteProfile(profile: ProfileFullSuit)
    */

    @Insert
    fun insertProfile(profile: ProfileFullSuit)

    @Delete
    fun deleteProfile(profile: ProfileFullSuit)

    @Update
    fun updateProfile(profile: ProfileFullSuit)

    @Query("select * from profile_table where fitMode=0 order by name ASC")
    fun allProfiles(): List<ProfileFullSuit>

    @Query("select * from profile_table where fitMode=1")
    fun easyProfiles(): List<ProfileFullSuit>

    @Query("select * from profile_table where fitMode=2")
    fun mediumProfiles(): List<ProfileFullSuit>

    @Query("select * from profile_table where fitMode=3")
    fun hardProfiles(): List<ProfileFullSuit>
}