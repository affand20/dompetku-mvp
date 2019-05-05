package org.trydev.apps.dompetku.view

import org.trydev.apps.dompetku.db.Transaksi

interface LaporanView {
    fun showPengeluaranBulanan(total:Int)
    fun showPemasukanBulanan(total:Int)
    fun showLaporanPemasukan(data:List<Transaksi>)
    fun showLaporanPengeluaran(data:List<Transaksi>)
}