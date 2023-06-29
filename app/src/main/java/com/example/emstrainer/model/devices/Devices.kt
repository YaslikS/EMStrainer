package com.example.emstrainer.model.devices

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Devices(
    val numberDevice: String,
    val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}