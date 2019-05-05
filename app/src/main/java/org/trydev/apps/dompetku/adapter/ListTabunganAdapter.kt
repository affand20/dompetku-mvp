package org.trydev.apps.dompetku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk15.coroutines.onClick
import org.trydev.apps.dompetku.R
import org.trydev.apps.dompetku.db.Tabungan
import java.text.NumberFormat
import java.util.*

class ListTabunganAdapter(private val list:List<Tabungan>, private val listener:(Tabungan)->Unit):RecyclerView.Adapter<ListTabunganAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_tabungan, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(list[position], listener)
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){

        private val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in","ID"))
        private val namaTabungan:TextView = view.find(R.id.nama_tabungan)
        private val sisaSaldo:TextView = view.find(R.id.sisa_saldo)

        fun bindItem(tabungan:Tabungan, listener: (Tabungan) -> Unit){
            namaTabungan.text = tabungan.tabunganName
            sisaSaldo.text = formatRupiah.format(tabungan.tabunganNominal.toDouble())
            itemView.onClick {
                listener(tabungan)
            }
        }
    }
}