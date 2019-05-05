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
import org.jetbrains.anko.sdk15.coroutines.onClick
import org.trydev.apps.dompetku.R
import org.trydev.apps.dompetku.model.Kategori

class KategoriAdapter(val listKategori:List<Kategori>, private val listener: (Kategori) -> Unit):RecyclerView.Adapter<KategoriAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.kategori_layout, parent, false))
    }

    override fun getItemCount() = listKategori.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(listKategori[position], listener)
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view) {
        private val icon:ImageView = view.find(R.id.icon_kategori)
        private val title:TextView = view.find(R.id.nama_kategori)

        fun bindItems(data:Kategori, listener:(Kategori)->Unit){
            icon.setImageDrawable(ContextCompat.getDrawable(itemView.context, data.icon))
            title.text = data.nama
            itemView.onClick {
                listener(data)
            }
        }
    }
}