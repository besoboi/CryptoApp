package com.example.cryptoapp.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CoinInfo(

    @SerializedName("Id")
    @Expose
    val id: String? = null,

    @SerializedName("Name")
    @Expose
    val name: String? = null

)