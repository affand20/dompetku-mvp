package org.trydev.apps.dompetku.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.find
import org.trydev.apps.dompetku.R
import org.trydev.apps.dompetku.db.Transaksi
import kotlinx.android.synthetic.main.transaksi_layout.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.sdk15.coroutines.onClick
import org.trydev.apps.dompetku.model.Kategori
import java.text.NumberFormat
import java.util.*

class TransaksiAdapter(private val transaksi:List<Transaksi>, private val listener:(Transaksi)->Unit):RecyclerView.Adapter<TransaksiAdapter.TransaksiHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiHolder {
        return TransaksiHolder(LayoutInflater.from(parent.context).inflate(R.layout.transaksi_layout, parent, false))
    }

    override fun getItemCount() = transaksi.size

    override fun onBindViewHolder(holder: TransaksiHolder, position: Int) {
        holder.bindItem(transaksi[position], listener)
    }

    class TransaksiHolder(view:View):RecyclerView.ViewHolder(view),AnkoLogger {

        private val judul:TextView = view.find(R.id.judul_transaksi)
        private val nominal:TextView = view.find(R.id.nominal_transaksi)
        private val tanggal:TextView = view.find(R.id.tanggal_transaksi)
        private val icon:ImageView = view.find(R.id.kategori_icon)

        private val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in","ID"))

        fun bindItem(data:Transaksi, listener: (Transaksi) -> Unit){
            if (data.jenis=="Pengeluaran"||data.jenis=="pengeluaran"){
                nominal.setTextColor(ContextCompat.getColor(itemView.context,R.color.pengeluaran))
            } else if (data.jenis=="Pemasukan"||data.jenis=="pemasukan"){
                nominal.setTextColor(ContextCompat.getColor(itemView.context,R.color.pemasukan))
            }
            when (data.kategori){
                "Makan" -> {
                    icon.setImageResource(R.drawable.burger)
                }
                "Transport" -> {
                    icon.setImageResource(R.drawable.transport)
                }
                "Belanja" -> {
                    icon.setImageResource(R.drawable.shopping)
                }
                "Lain-lain" -> {
                    icon.setImageResource(R.drawable.uncategorized)
                }
            }
            judul.text = data.judul
            nominal.text = formatRupiah.format(data.nominal?.toDouble())
            tanggal.text = data.tanggal

            itemView.onClick {
                listener(data)
            }
        }
    }

}
