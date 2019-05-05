package org.trydev.apps.dompetku.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_target_tabungan.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.sdk15.coroutines.onClick
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.startActivity
import org.trydev.apps.dompetku.R
import org.trydev.apps.dompetku.adapter.TabunganAdapter
import org.trydev.apps.dompetku.db.TargetTabungan
import org.trydev.apps.dompetku.presenter.TabunganPresenter

class TabunganFragment: Fragment(), TabunganView, AnkoLogger {
    private lateinit var presenter:TabunganPresenter

    private lateinit var tabunganAdapter:TabunganAdapter

    private var targetTabungan:MutableList<TargetTabungan> = mutableListOf()

    private var totalSaldo: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_target_tabungan,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        app_toolbar?.title = "Target Tabungan"

        presenter = TabunganPresenter(this, ctx)
        presenter.getCurrentTercapai()
        presenter.getTotalTarget()
        presenter.getListTabungan()
        presenter.getTotalSaldo()

        list_target_tabungan.layoutManager = LinearLayoutManager(ctx)

        btn_lihat_semua.onClick {
            startActivity<ListTabunganActivity>()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter = TabunganPresenter(this, ctx)
        presenter.getCurrentTercapai()
        presenter.getTotalTarget()
        presenter.getListTabungan()
        presenter.getTotalSaldo()
    }

    override fun showCurrentTercapai(total: Int) {
        current_tercapai.text = total.toString()
    }

    override fun showTotalTarget(total: Int) {
        total_target.text = total.toString()
    }

    override fun showListTabungan(data: List<TargetTabungan>) {
        data.let{
            
            targetTabungan.clear()
            targetTabungan.addAll(it)
            tabunganAdapter.notifyDataSetChanged()
        }
    }

    override fun getTotalSaldo(total: Int) {
        totalSaldo = total
        info("TOTAL SALDO $totalSaldo")
        tabunganAdapter = TabunganAdapter(targetTabungan, totalSaldo){
        }
        list_target_tabungan.adapter = tabunganAdapter
    }
}
