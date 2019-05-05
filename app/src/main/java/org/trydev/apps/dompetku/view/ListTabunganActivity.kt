package org.trydev.apps.dompetku.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_list_tabungan.*

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.support.v4.ctx
import org.trydev.apps.dompetku.R
import org.trydev.apps.dompetku.adapter.ListTabunganAdapter
import org.trydev.apps.dompetku.adapter.TabunganAdapter
import org.trydev.apps.dompetku.db.TargetTabungan
import org.trydev.apps.dompetku.db.Transaksi
import org.trydev.apps.dompetku.presenter.TabunganPresenter

class ListTabunganActivity : AppCompatActivity(),TabunganView,AnkoLogger {

    private lateinit var adapter:TabunganAdapter
    private lateinit var presenter:TabunganPresenter
    private var targetTabungan:MutableList<TargetTabungan> = mutableListOf()
    private var totalSaldo = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,false)
        window.statusBarColor = Color.TRANSPARENT

        setContentView(R.layout.activity_list_tabungan)
        app_toolbar?.title = "Target Tabungan"



        list_target_tabungan.layoutManager = LinearLayoutManager(this)
        presenter = TabunganPresenter(this,this)
        presenter.getListTabungan()
        presenter.getTotalSaldo()
        presenter.getTotalTarget()
        presenter.getCurrentTercapai()
    }

    override fun onResume() {
        super.onResume()
        presenter = TabunganPresenter(this, this)
        presenter.getCurrentTercapai()
        presenter.getTotalTarget()
        presenter.getAllListTabungan()
        presenter.getTotalSaldo()
    }

    override fun showCurrentTercapai(total: Int) {

    }

    override fun showTotalTarget(total: Int) {

    }

    override fun showListTabungan(data: List<TargetTabungan>) {
        data.let{
            targetTabungan.clear()
            targetTabungan.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }

    override fun getTotalSaldo(total: Int) {
        totalSaldo = total
        info("TOTAL SALDO $totalSaldo")
        adapter = TabunganAdapter(targetTabungan, totalSaldo){

        }
        list_target_tabungan.adapter = adapter
    }

}
