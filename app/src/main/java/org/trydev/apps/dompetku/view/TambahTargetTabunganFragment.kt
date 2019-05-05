package org.trydev.apps.dompetku.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stepstone.stepper.BlockingStep
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_tambah_target_tabungan.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.sdk15.coroutines.textChangedListener
import org.jetbrains.anko.support.v4.toast
import org.trydev.apps.dompetku.R
import org.trydev.apps.dompetku.db.TargetTabungan
import org.trydev.apps.dompetku.db.database
import java.text.NumberFormat
import java.util.*

class TambahTargetTabunganFragment:Fragment(),BlockingStep, AnkoLogger {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tambah_target_tabungan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in","ID"))

        judul.textChangedListener {
            afterTextChanged {
                judul_tabungan.text = judul.text.toString()
            }
        }
        nominal.textChangedListener {
            afterTextChanged{
                if (!nominal.text.toString().isEmpty()){
                    target_tabungan.text = formatRupiah.format(nominal.text.toString().toDouble())
                } else{
                    target_tabungan.text = "Rp0"
                }

            }
        }
    }

    override fun onBackClicked(callback: StepperLayout.OnBackClickedCallback?) {
        callback?.goToPrevStep()
    }

    override fun onSelected() {

    }

    override fun onCompleteClicked(callback: StepperLayout.OnCompleteClickedCallback?) {
        if (!judul.text.toString().isEmpty() && !nominal.text.toString().isEmpty()){
            context?.database?.use {
                insert(TargetTabungan.TABLE_TARGET,
                        TargetTabungan.TARGET_JUDUL to judul.text.toString(),
                        TargetTabungan.TARGET_TOTAL to nominal.text.toString(),
                        TargetTabungan.STATUS to "OnGoing"
                )
            }
            toast("Berhasil ditambahkan")
            callback?.complete()
        } else{
            toast("Mohon lengkapi semua input")
        }
    }

    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        callback?.goToNextStep()
    }

    override fun verifyStep() = null

    override fun onError(error: VerificationError) {

    }
}