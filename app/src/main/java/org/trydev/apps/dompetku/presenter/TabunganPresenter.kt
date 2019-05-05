package org.trydev.apps.dompetku.presenter

import android.content.Context
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.selects.select
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.db.SqlOrderDirection
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.info
import org.trydev.apps.dompetku.db.Tabungan
import org.trydev.apps.dompetku.db.database
import org.trydev.apps.dompetku.db.TargetTabungan
import org.trydev.apps.dompetku.view.TabunganView

class TabunganPresenter(private val view:TabunganView, private val context: Context):AnkoLogger {
    fun getTotalTarget(){
        async(UI){
            val total = bg{
                context.database.use {
                    select(TargetTabungan.TABLE_TARGET).parseList(classParser<TargetTabungan>())
                }
            }
            view.showTotalTarget(total.await().size)
        }
    }

    fun getCurrentTercapai(){
        async(UI){
            val total = bg{
                context.database.use {
                    select(TargetTabungan.TABLE_TARGET).whereArgs("${TargetTabungan.STATUS}== 'Tercapai'").parseList(classParser<TargetTabungan>())
                }
            }
            view.showCurrentTercapai(total.await().size)
        }
    }

    fun getListTabungan(){
        async(UI){
            val data = bg{
                context?.database?.use {
                    select(TargetTabungan.TABLE_TARGET).whereArgs(("${TargetTabungan.STATUS} == 'OnGoing'")).parseList(classParser<TargetTabungan>())
                }
            }
            view.showListTabungan(data.await())
        }

    }

    fun getAllListTabungan(){
        async(UI){
            val data = bg{
                context?.database?.use {
                    select(TargetTabungan.TABLE_TARGET).orderBy(TargetTabungan.STATUS, SqlOrderDirection.ASC).parseList(classParser<TargetTabungan>())
                }
            }
            view.showListTabungan(data.await())
        }
    }

    fun getTotalSaldo(){
        async(UI){
            val data = bg {
                context?.database?.use {
                    select(Tabungan.TABLE_TABUNGAN).parseList(classParser<Tabungan>())
                }
            }
            var total = 0
            for (i in data.await().indices){
                total += data.await()[i].tabunganNominal
            }
            view.getTotalSaldo(total)
        }
    }
}