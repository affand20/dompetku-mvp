package org.trydev.apps.dompetku.view

import org.trydev.apps.dompetku.db.Tabungan
import org.trydev.apps.dompetku.db.Transaksi

interface MainView {
    fun showCardList(card:List<Tabungan>?)
    fun showTransaksi(data:List<Transaksi>?)
    fun showTotalUang(total:Int)
    fun showPemasukanHarian(total:Int)
    fun showPengeluaranHarian(total:Int)
}