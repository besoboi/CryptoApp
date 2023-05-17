package com.example.cryptoapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "full_price_list")
data class CoinInfoDbModel(
    @PrimaryKey
    val fromsymbol: String,
    val tosymbol: String?,
    val price: Double?,
    val lastupdate: Long?,
    val highday: Double?,
    val lowday: Double?,
    val lastmarket: String?,
    val imageurl: String
)