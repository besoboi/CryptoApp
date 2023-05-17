package com.example.cryptoapp.domain

data class CoinInfo(
    val fromsymbol: String,
    val tosymbol: String?,
    val price: Double?,
    val lastupdate: String,
    val highday: Double?,
    val lowday: Double?,
    val lastmarket: String?,
    val imageurl: String
)