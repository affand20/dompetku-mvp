package org.trydev.apps.dompetku.view


import org.trydev.apps.dompetku.db.TargetTabungan

interface TabunganView {
    fun showCurrentTercapai(total: Int)
    fun showTotalTarget(total: Int)
    fun showListTabungan(data: List<TargetTabungan>)
    fun getTotalSaldo(total: Int)
}