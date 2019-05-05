package org.trydev.apps.dompetku.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.trydev.apps.dompetku.R
import org.trydev.apps.dompetku.db.TargetTabungan
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import org.jetbrains.anko.find
import org.jetbrains.anko.info
import org.jetbrains.anko.sdk15.coroutines.onClick
import org.trydev.apps.dompetku.db.database
import java.text.NumberFormat
import java.util.*
import kotlin.math.abs

class TabunganAdapter(private val data:List<TargetTabungan>, private val totalSaldo:Int, private val listener:(TargetTabungan)->Unit):RecyclerView.Adapter<TabunganAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.target_tabungan_layout, parent, false))
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(data[position], totalSaldo, listener)
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view),AnkoLogger {

        private val judul:TextView = view.find(R.id.judul_tabungan)
        private val total:TextView = view.find(R.id.target_tabungan)
        private val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in","ID"))
        private val progress:View = view.find(R.id.progress)
        private val btn:Button = view.find(R.id.btn_set_tercapai)
        private val btnTercapai:Button = view.find(R.id.btn_tercapai)

        fun bindItems(target:TargetTabungan, totalSaldo: Int, listener: (TargetTabungan) -> Unit){
            if (target.status!="OnGoing"){
                btn.visibility = View.GONE
                btnTercapai.visibility = View.VISIBLE
            }
            judul.text = target.judulTarget
            total.text = formatRupiah.format(target.totalTarget.toDouble())
            val display:WindowManager = itemView.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            if (target.totalTarget<totalSaldo || target.status!="OnGoing"){
                progress.layoutParams.width = display.defaultDisplay.width
                total.setTextColor(ContextCompat.getColor(itemView.context, R.color.pemasukan))
            } else{
                progress.layoutParams.width = display.defaultDisplay.width * abs((totalSaldo*100/target.totalTarget)) / 100
            }

            btn.onClick {
                itemView.context.database.use {
                    update(TargetTabungan.TABLE_TARGET, TargetTabungan.STATUS to "Tercapai").whereArgs("${TargetTabungan.ID} = {targetId}", "targetId" to target.id).exec()
                }
            }

            itemView.onClick {
                listener(target)
            }
        }
    }
}