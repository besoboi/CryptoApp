package com.example.cryptoapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cryptoapp.api.ApiFactory
import com.example.cryptoapp.database.AppDatabase
import com.example.cryptoapp.pojo.CoinPriceInfo
import com.example.cryptoapp.pojo.CoinPriceInfoRawData
import com.google.gson.Gson
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CoinViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    val priceList = db.coinPriceInfoDao().getPriceList()
    private val compositeDisposable = CompositeDisposable()

    init {
        loadData()
    }

    fun getDetailInfo(fSym: String): LiveData<CoinPriceInfo> {
        return db.coinPriceInfoDao().getPriceInfoAboutCoin(fSym)
    }

    private fun loadData() {
        val disposable = ApiFactory.apiService.getTopCoinsInfo(limit = 50)
            ?.map { it.data?.map { it.coinInfo?.name }?.joinToString(",").toString() }
            ?.flatMap { ApiFactory.apiService.getFullPriceList(fSyms = it) }
            ?.map { getPriceListFromRawData(it) }
            ?.delaySubscription(10, TimeUnit.SECONDS)
            ?.repeat()
            ?.retry()
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({
                if (it != null) {
                    db.coinPriceInfoDao().insertPriceList(it)
                }
                Log.d("netData", "Success: " + it.toString())
            }, {
                Log.d("netData", "Failure: " + it.message.toString())
            })
        if (disposable != null) {
            compositeDisposable.add(disposable)
        }
    }

    private fun getPriceListFromRawData(
        coinPriceInfoRawData: CoinPriceInfoRawData
    ): List<CoinPriceInfo>? {
        val result = ArrayList<CoinPriceInfo>()
        val jsonObject = coinPriceInfoRawData.coinPriceInfoJsonObject ?: return null
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinPriceInfo::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}