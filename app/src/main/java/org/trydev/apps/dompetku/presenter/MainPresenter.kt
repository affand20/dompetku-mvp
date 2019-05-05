package org.trydev.apps.dompetku.presenter

import android.content.Context
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.selects.select
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.info
import org.trydev.apps.dompetku.db.database

import org.trydev.apps.dompetku.db.Tabungan

import org.trydev.apps.dompetku.db.Transaksi
import org.trydev.apps.dompetku.view.MainView
import java.util.*

class MainPresenter(private val view:MainView, private val context:Context):AnkoLogger {

    private val listDay = arrayOf("Minggu","Senin","Selasa","Rabu","Kamis","Jum'at","Sabtu")
    private val listMonth = arrayOf("Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember")

    fun getAllCard(){
        async(UI){
            val data = bg{
                context?.database?.use{
                    select(Tabungan.TABLE_TABUNGAN).parseList(classParser<Tabungan>())
                }
            }
            var total = 0
            for (i in data.await().indices){
                total += data.await()[i].tabunganNominal
            }
            view.showCardList(data.await())
            view.showTotalUang(total)
        }
    }

    fun getLastTransaksi(){
        async(UI){
            val data = bg{
                context.database.use {
                    select(Transaksi.TABLE_TRANSAKSI).limit(10).parseList(classParser<Transaksi>())
                }
            }
            view.showTransaksi(data.await())
        }

    }

    fun getPemasukanHarian(){
        async (UI){
            var total = 0
            val calendar = Calendar.getInstance()
            val now = "${listDay[calendar.get(Calendar.DAY_OF_WEEK)-1]}, ${calendar.get(Calendar.DATE)} ${listMonth[calendar.get(Calendar.MONTH)]} ${calendar.get(Calendar.YEAR)}"
            info("TEST: $now")
            val pemasukan = bg{
                context.database.use {
                    select(Transaksi.TABLE_TRANSAKSI).whereArgs("(${Transaksi.TRANSAKSI_JENIS} == 'Pemasukan') and (${Transaksi.TRANSAKSI_TANGGAL} == {tanggal})", "tanggal" to now).parseList(classParser<Transaksi>())
                }
            }
            for (i in pemasukan.await().indices){
                total += pemasukan.await()[i].nominal
            }

            view.showPemasukanHarian(total)
        }

    }

    fun getPengeluaranHarian(){
        async (UI){
            var total = 0
            val calendar = Calendar.getInstance()
            val now = "${listDay[calendar.get(Calendar.DAY_OF_WEEK)-1]}, ${calendar.get(Calendar.DATE)} ${listMonth[calendar.get(Calendar.MONTH)]} ${calendar.get(Calendar.YEAR)}"
            val pengeluaran = bg{
                context.database.use {
                    select(Transaksi.TABLE_TRANSAKSI).whereArgs("(${Transaksi.TRANSAKSI_JENIS} == 'Pengeluaran') and (${Transaksi.TRANSAKSI_TANGGAL} == {tanggal})", "tanggal" to now).parseList(classParser<Transaksi>())
                }
            }
            info("PENGELUARAN SIZE ${pengeluaran.await().size}")
            for (i in pengeluaran.await().indices){
                total += pengeluaran.await()[i].nominal
            }
            view.showPengeluaranHarian(total)
        }
    }
}