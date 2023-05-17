package com.example.cryptoapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.pojo.CoinPriceInfo
import com.squareup.picasso.Picasso

class CoinInfoAdapter(private val context: Context) :
    RecyclerView.Adapter<CoinInfoAdapter.CoinInfoViewHolder>() {

    var coinInfoList: List<CoinPriceInfo> = listOf<CoinPriceInfo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onCoinClickListener: OnCoinClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_coin_info, parent, false
        )
        return CoinInfoViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CoinInfoViewHolder, position: Int) {
        val coin = coinInfoList[position]
        with(holder) {
            with(coin) {
                val symbolsTemplate = context.getString(R.string.symbols_template)
                val lastUpdateTimeTemplate = context.getString(R.string.last_update_time)
                tvSymbols.text = String.format(symbolsTemplate, fromsymbol, tosymbol)
                tvPrice.text = price.toString()
                tvUpdateTime.text = String.format(lastUpdateTimeTemplate, getFormattedTime())
                Picasso.get().load(getFullImageUrl()).into(ivLogoCoin)
                itemView.setOnClickListener() {
                    onCoinClickListener?.onCoinClick(this)
                }
            }
        }

    }

    override fun getItemCount() = coinInfoList.size

    interface OnCoinClickListener {
        fun onCoinClick(coinPriceInfo: CoinPriceInfo)
    }

    inner class CoinInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivLogoCoin = itemView.findViewById<ImageView>(R.id.ivLogoCoin)
        val tvSymbols = itemView.findViewById<TextView>(R.id.tvSymbols)
        val tvPrice = itemView.findViewById<TextView>(R.id.tvPrice)
        val tvUpdateTime = itemView.findViewById<TextView>(R.id.tvUpdateTime)
    }
}