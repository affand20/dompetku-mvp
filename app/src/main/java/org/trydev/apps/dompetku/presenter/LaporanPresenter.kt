package org.trydev.apps.dompetku.presenter

import android.content.Context
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.trydev.apps.dompetku.db.database
import org.trydev.apps.dompetku.db.Transaksi
import org.trydev.apps.dompetku.view.LaporanView
import java.util.*

class LaporanPresenter(private val view:LaporanView, private val context: Context) {


    fun getLaporanPemasukanBulanan(){
        async(UI){
            val data = bg {
                context?.database?.use {
                    select(Transaksi.TABLE_TRANSAKSI).whereArgs(("${Transaksi.TRANSAKSI_JENIS}=='Pemasukan'")).parseList(classParser<Transaksi>())
                }
            }
            var total = 0
            for (i in data.await().indices){
                total += data.await()[i].nominal
            }
            view.showLaporanPemasukan(data.await())
            view.showPemasukanBulanan(total)
        }
    }
    fun getLaporanPengeluaranBulanan(){
        async(UI){
            val data = bg {
                context?.database?.use {
                    select(Transaksi.TABLE_TRANSAKSI).whereArgs(("${Transaksi.TRANSAKSI_JENIS}=='Pengeluaran'")).parseList(classParser<Transaksi>())
                }
            }
            var total = 0
            for (i in data.await().indices){
                total += data.await()[i].nominal
            }
            view.showPengeluaranBulanan(total)
            view.showLaporanPengeluaran(data.await())
        }
    }
}