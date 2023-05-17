package com.example.cryptoapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso

class CoinDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: CoinViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_detail)
        val ivLargeLogoCoin = findViewById<ImageView>(R.id.ivLargeLogoCoin)
        val tvFromSymbol = findViewById<TextView>(R.id.tvFromSymbol)
        val tvToSymbol = findViewById<TextView>(R.id.tvToSymbol)
        val tvPriceValue = findViewById<TextView>(R.id.tvPriceValue)
        val tvMinDayValue = findViewById<TextView>(R.id.tvMinDayValue)
        val tvMaxDayValue = findViewById<TextView>(R.id.tvMaxDayValue)
        val tvLastBuyValue = findViewById<TextView>(R.id.tvLastBuyValue)
        val tvUpdatedValue = findViewById<TextView>(R.id.tvUpdatedValue)

        if (!intent.hasExtra(EXTRA_FROM_SYMBOL)) {
            finish()
        }
        if (!intent.hasExtra(EXTRA_FROM_SYMBOL)) {
            finish()
            return
        }
        val fromSymbol = intent.getStringExtra(EXTRA_FROM_SYMBOL)
        viewModel = ViewModelProvider(this)[CoinViewModel::class.java]
        if (fromSymbol != null) {
            viewModel.getDetailInfo(fromSymbol).observe(this, Observer {
                Picasso.get().load(it.getFullImageUrl()).into(ivLargeLogoCoin)
                tvFromSymbol.text = it.fromsymbol
                tvToSymbol.text = it.tosymbol
                tvPriceValue.text = it.price.toString()
                tvMinDayValue.text = it.lowday.toString()
                tvMaxDayValue.text = it.highday.toString()
                tvLastBuyValue.text = it.lastmarket
                tvUpdatedValue.text = it.getFormattedTime()
            })
        }
    }

    companion object {
        private const val EXTRA_FROM_SYMBOL = "fSym"

        fun newIntent(context: Context, fromSymbol: String): Intent {
            val intent = Intent(context, CoinDetailActivity::class.java)
            intent.putExtra(EXTRA_FROM_SYMBOL, fromSymbol)
            return intent
        }
    }
}