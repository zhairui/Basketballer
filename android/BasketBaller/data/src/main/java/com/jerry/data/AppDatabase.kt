package com.jerry.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jerry.data.dao.NewsDao
import com.jerry.data.entity.NewsEntity

@Database(entities = [NewsEntity::class],version = 1)
abstract class AppDatabase :RoomDatabase(){

    abstract fun getNewsDao() : NewsDao
    companion object {

        @Volatile private var instance:AppDatabase? =null

        fun getInstance(context: Context):AppDatabase{
            return instance?: synchronized(this){
                instance?:buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context):AppDatabase{
            return Room.databaseBuilder(context,AppDatabase::class.java,"basketball").build()
        }
    }
}