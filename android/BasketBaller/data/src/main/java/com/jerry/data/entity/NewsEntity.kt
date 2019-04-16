package com.jerry.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(@PrimaryKey val id:String, val title:String, val article_url:String, val images:String)