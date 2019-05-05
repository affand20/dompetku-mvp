package org.trydev.apps.dompetku.view

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stepstone.stepper.BlockingStep
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_tambah_kartu.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.sdk15.coroutines.onClick
import org.jetbrains.anko.sdk15.coroutines.textChangedListener
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.toast
import org.trydev.apps.dompetku.R
import org.trydev.apps.dompetku.db.Tabungan
import org.trydev.apps.dompetku.db.database
import java.text.NumberFormat
import java.util.*

class TambahKartuFragment:Fragment(), BlockingStep, AnkoLogger {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tambah_kartu, container, false)
    }

    val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in","ID"))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if (arguments?.getString("type")=="Edit"){
//            submit.visibility = View.VISIBLE
//            val cardName = arguments?.getString("cardName")
//            val cardNominal = arguments?.getInt("cardNominal")
//            card_name.text = cardName
//            nama_kartu.setText(cardName)
//            nominal.text = formatRupiah.format(cardNominal?.toDouble())
//            nominal_kartu.setText(cardNominal.toString())
//        }

        nama_kartu.textChangedListener {
            afterTextChanged {
                card_name.text = nama_kartu.text.toString()
            }
        }



        nominal.text = String.format(resources.getString(R.string.default_nominal_format), 0)

        nominal_kartu.textChangedListener {
            afterTextChanged {

                if (!nominal_kartu.text.toString().isEmpty()&&nominal_kartu.text.toString().length<10){
                    nominal.text = formatRupiah.format(nominal_kartu.text.toString().toInt().toDouble())
                } else if(nominal_kartu.text.toString().length>=10){
                    toast("Masukkan angka yang lebih kecil")
                } else{
                    nominal.text = String.format(resources.getString(R.string.default_nominal_format),0)
                }

            }
        }

        submit.onClick {
            if (!nominal_kartu.text.toString().isEmpty() && !nama_kartu.text.toString().isEmpty()){
                ctx.database.use {
                    insert(
                            Tabungan.TABLE_TABUNGAN,
                            Tabungan.TABUNGAN_NAME to nama_kartu.text.toString(),
                            Tabungan.TABUNGAN_NOMINAL to nominal_kartu.text.toString().toInt()
                    )
                    toast("Berhasil ditambahkan")
                }
            } else{
                toast("Mohon lengkapi semua inputan")
            }
        }
    }

    override fun onSelected() {

    }

    override fun verifyStep() = null

    override fun onError(error: VerificationError){
    }

    override fun onBackClicked(callback: StepperLayout.OnBackClickedCallback?) {
        callback?.goToPrevStep()
    }

    override fun onCompleteClicked(callback: StepperLayout.OnCompleteClickedCallback?) {
        if (!nominal_kartu.text.toString().isEmpty() && !nama_kartu.text.toString().isEmpty()){
            ctx.database.use {
                insert(
                        Tabungan.TABLE_TABUNGAN,
                        Tabungan.TABUNGAN_NAME to nama_kartu.text.toString(),
                        Tabungan.TABUNGAN_NOMINAL to nominal_kartu.text.toString().toInt()
                )
                toast("Berhasil ditambahkan")
                callback?.complete()
            }
        } else{
            toast("Mohon lengkapi semua inputan")
        }

    }

    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        Handler().postDelayed({
            callback?.goToNextStep()
        },0L)
    }
}