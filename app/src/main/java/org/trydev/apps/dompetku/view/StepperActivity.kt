package org.trydev.apps.dompetku.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.stepper.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.trydev.apps.dompetku.R
import org.trydev.apps.dompetku.adapter.StepperAdapter

class StepperActivity : AppCompatActivity(), StepperLayout.StepperListener, AnkoLogger {

    lateinit var key: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stepper)

        val stepper:StepperLayout = find(R.id.stepper_layout)

        val getIntent = intent.getStringExtra("fragment")
        info(getIntent)

        when (getIntent){
            "TAMBAH KARTU" -> {
                key = "TAMBAH KARTU"
            }
            "TAMBAH PENGELUARAN" -> {
                key = "TAMBAH PENGELUARAN"
            }
            "TAMBAH PEMASUKAN" -> {
                key = "TAMBAH PEMASUKAN"
            }
            "TAMBAH TARGET TABUNGAN" -> {
                key = "TAMBAH TARGET TABUNGAN"
            }
        }

        stepper.adapter = StepperAdapter(supportFragmentManager, this, key)
        stepper.setListener(this)

    }

    override fun onStepSelected(newStepPosition: Int) {
//        toast("onStepSelected -> $newStepPosition")
    }

    override fun onError(verificationError: VerificationError?) {
        toast("onError -> ${verificationError?.errorMessage}")
    }

    override fun onReturn() {
        finish()
    }

    override fun onCompleted(completeButton: View?) {
//        toast("onComplete!")
        onReturn()
    }
}
