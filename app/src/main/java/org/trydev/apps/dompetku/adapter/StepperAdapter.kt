package org.trydev.apps.dompetku.adapter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel
import org.trydev.apps.dompetku.view.StepFragment
import org.trydev.apps.dompetku.view.TambahKartuFragment
import org.trydev.apps.dompetku.view.TambahTargetTabunganFragment
import org.trydev.apps.dompetku.view.TambahTransaksiFragment

class StepperAdapter(private val fm:FragmentManager, private val ctx:Context, private val key: String):AbstractFragmentStepAdapter(fm, ctx) {

    private var CURRENT_STEP_POSITION_KEY:String = "messageResourceId"

    override fun getCount() = 1

    override fun createStep(position: Int): Step {
        when (key){
            "TAMBAH KARTU" -> {
                val step = TambahKartuFragment()
                val bundle = Bundle()
                bundle.putInt(CURRENT_STEP_POSITION_KEY, position)
                step.arguments = bundle
                return step
            }
            "TAMBAH PENGELUARAN" -> {
                val step = TambahTransaksiFragment()
                val bundle = Bundle()
                bundle.putInt(CURRENT_STEP_POSITION_KEY, position)
                bundle.putString("JENIS_TRANSAKSI","Pengeluaran")
                step.arguments = bundle
                return step
            }
            "TAMBAH PEMASUKAN" -> {
                val step = TambahTransaksiFragment()
                val bundle = Bundle()
                bundle.putInt(CURRENT_STEP_POSITION_KEY, position)
                bundle.putString("JENIS_TRANSAKSI", "Pemasukan")
                step.arguments = bundle
                return step
            }
            "TAMBAH TARGET TABUNGAN" -> {
                val step = TambahTargetTabunganFragment()
                val bundle = Bundle()
                bundle.putInt(CURRENT_STEP_POSITION_KEY, position)
                step.arguments = bundle
                return step
            }

        }
        val step = StepFragment()
        val bundle = Bundle()
        bundle.putInt(CURRENT_STEP_POSITION_KEY, position)
        step.arguments = bundle
        return step
    }

    override fun getViewModel(position: Int): StepViewModel {
        return StepViewModel.Builder(ctx)
                .setTitle("Tambah Kartu")
                .create()
    }
}