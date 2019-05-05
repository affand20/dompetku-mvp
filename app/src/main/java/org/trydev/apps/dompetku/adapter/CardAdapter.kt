package org.trydev.apps.dompetku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.trydev.apps.dompetku.R
import org.trydev.apps.dompetku.db.Tabungan
import kotlinx.android.synthetic.main.card_layout.*
import kotlinx.android.synthetic.main.card_layout.view.*
import org.jetbrains.anko.sdk15.coroutines.onClick
import java.text.NumberFormat
import java.util.*

class CardAdapter(private val tabungan:List<Tabungan>, private val listener:(Tabungan)->Unit):RecyclerView.Adapter<CardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false))
    }

    override fun getItemCount()= tabungan.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(tabungan[position] , listener)
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        private val nama:TextView = view.card_name
        private val nominal:TextView = view.nominal

        private val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in","ID"))

        fun bindItems(tabungan: Tabungan, listener: (Tabungan) -> Unit){
            nama.text = tabungan.tabunganName
            nominal.text = formatRupiah.format(tabungan.tabunganNominal.toDouble())
            itemView.onClick {
                listener(tabungan)
            }
        }
    }
}