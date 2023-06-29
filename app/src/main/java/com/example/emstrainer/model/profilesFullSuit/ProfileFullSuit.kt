package com.example.emstrainer.model.profilesFullSuit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_table")
data class ProfileFullSuit(
    val name: String,
    var fitMode: Int, // 0-normal | 1-easy | 2-medium | 3-hard
    var radioId: Int?,
    val modulationFm: Boolean,
    val modulationAm: Boolean,
    val powerChange: Int,
    var thirdByteCommand: Byte = 0,
    val maxPower: Int,
    val power1: Int,
    val power2: Int,
    val power3: Int,
    val power4: Int,
    val power5: Int,
    val power6: Int,
    val power7: Int,
    val power8: Int,
    val power9: Int,
    val power10: Int,
    val power11: Int,
    val power12: Int,
    val times: Int,
    val animationNumber: Int,
    val typeTimes: Boolean, // false - время | true - количество
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}