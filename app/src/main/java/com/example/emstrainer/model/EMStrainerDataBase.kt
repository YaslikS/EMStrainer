package com.example.emstrainer.model

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.emstrainer.model.devices.Devices
import com.example.emstrainer.model.devices.DevicesDao
import com.example.emstrainer.model.profilesFullSuit.ProfileFullSuit
import com.example.emstrainer.model.profilesFullSuit.ProfileFullSuitDao

@Database(
    entities = [Devices::class, ProfileFullSuit::class], version = 9
)
abstract class EMStrainerDataBase : RoomDatabase() {
    abstract fun devicesDao(): DevicesDao
    abstract fun profileFullSuitDao(): ProfileFullSuitDao

    companion object {
        fun get(application: Application): EMStrainerDataBase {
            return Room.databaseBuilder(
                application, EMStrainerDataBase::class.java, "emst_database"
            ).fallbackToDestructiveMigration().build()
        }
    }

    /*
    companion object{
        @Volatile
        private var INSTANCE: EMStrainerDataBase? = null

        fun getDB(context: Context): EMStrainerDataBase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance =  Room.databaseBuilder(
                    context.applicationContext,
                    EMStrainerDataBase::class.java,
                    "emst_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
    */

}