package org.trydev.apps.dompetku.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_laporan.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.sdk15.coroutines.onClick
import org.jetbrains.anko.support.v4.ctx
import org.trydev.apps.dompetku.R
import org.trydev.apps.dompetku.adapter.TransaksiAdapter
import org.trydev.apps.dompetku.model.TargetTabungan
import org.trydev.apps.dompetku.db.Transaksi
import org.trydev.apps.dompetku.db.database
import org.trydev.apps.dompetku.presenter.LaporanPresenter

class LaporanFragment:Fragment(),LaporanView {

    private lateinit var presenter:LaporanPresenter
    private var laporan:MutableList<Transaksi> = mutableListOf()
    private lateinit var adapter:TransaksiAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_laporan, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        app_toolbar?.title = "Laporan"

        adapter = TransaksiAdapter(laporan){

        }
        list_laporan.layoutManager = LinearLayoutManager(ctx)
        list_laporan.adapter = adapter

        presenter = LaporanPresenter(this, ctx)
        presenter.getLaporanPemasukanBulanan()
//        presenter.getLaporanPengeluaranBulanan()

        show_pemasukan.onClick {
            presenter.getLaporanPemasukanBulanan()
            sub_title_laporan.text = "Total Pemasukan"
            show_pemasukan.setTextColor(ContextCompat.getColor(ctx, android.R.color.white))
            show_pengeluaran.setTextColor(ContextCompat.getColor(ctx, R.color.progress_background))
        }

        show_pengeluaran.onClick {
            presenter.getLaporanPengeluaranBulanan()
            sub_title_laporan.text = "Total Pengeluaran"
            show_pengeluaran.setTextColor(ContextCompat.getColor(ctx, android.R.color.white))
            show_pemasukan.setTextColor(ContextCompat.getColor(ctx, R.color.progress_background))
        }
    }

    override fun onResume() {
        super.onResume()
        presenter = LaporanPresenter(this, ctx)
        presenter.getLaporanPemasukanBulanan()
    }

    override fun showPengeluaranBulanan(total:Int) {
        nominal.text = "Rp "+total.toString()
    }

    override fun showPemasukanBulanan(total:Int) {
        nominal.text = "Rp "+total.toString()
    }

    override fun showLaporanPemasukan(data:List<Transaksi>) {
        data.let{
            laporan.clear()
            laporan.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }

    override fun showLaporanPengeluaran(data:List<Transaksi>) {
        data.let{
            laporan.clear()
            laporan.addAll(it)
            adapter.notifyDataSetChanged()
        }
    }
}