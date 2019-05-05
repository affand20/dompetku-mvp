package org.trydev.apps.dompetku.view


import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stepstone.stepper.BlockingStep
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.fragment_tambah_transaksi.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update
import org.jetbrains.anko.find
import org.jetbrains.anko.info
import org.jetbrains.anko.sdk15.coroutines.onClick
import org.jetbrains.anko.sdk15.coroutines.onFocusChange
import org.jetbrains.anko.sdk15.coroutines.textChangedListener
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.toast
import org.trydev.apps.dompetku.R
import org.trydev.apps.dompetku.adapter.KategoriAdapter
import org.trydev.apps.dompetku.adapter.ListTabunganAdapter
import org.trydev.apps.dompetku.db.Tabungan
import org.trydev.apps.dompetku.db.Transaksi
import org.trydev.apps.dompetku.db.database
import org.trydev.apps.dompetku.model.Kategori
import java.text.DateFormat
import java.text.NumberFormat
import java.util.*

class TambahTransaksiFragment:Fragment(), BlockingStep, AnkoLogger {

    private lateinit var kategoriAdapter:KategoriAdapter
    private var tabunganTerpilih:String = ""
    private lateinit var listTabunganAdapter: ListTabunganAdapter
    private var nominalTabungan = 0
    private var idTabungan:Long = 0
    private var kategori:String = "Lain-lain"
    private val listDay = arrayOf("Minggu","Senin","Selasa","Rabu","Kamis","Jum'at","Sabtu")
    private val listMonth = arrayOf("Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tambah_transaksi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list_kategori.layoutManager = LinearLayoutManager(ctx, LinearLayout.HORIZONTAL, false)

        if (arguments?.getString("JENIS_TRANSAKSI").toString()=="Pengeluaran"){
            title_transaksi.text = "Tambah Pengeluaran"
            nominal_transaksi.setTextColor(ContextCompat.getColor(ctx,R.color.pengeluaran))
        } else if (arguments?.getString("JENIS_TRANSAKSI").toString()=="Pemasukan"){
            title_transaksi.text = "Tambah Pemasukan"
            nominal_transaksi.setTextColor(ContextCompat.getColor(ctx,R.color.pemasukan))
        }
        kategoriAdapter = KategoriAdapter(listOf(Kategori("Makan",R.drawable.burger),Kategori("Transport",R.drawable.transport),Kategori("Belanja",R.drawable.shopping),Kategori("Lain-lain",R.drawable.uncategorized))){
            kategori = it.nama
            kategori_icon.setImageDrawable(ContextCompat.getDrawable(ctx,it.icon))
        }

        list_kategori.adapter = kategoriAdapter

        val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in","ID"))

        judul.textChangedListener {
            afterTextChanged {
                judul_transaksi.text = judul.text.toString()
            }
        }
        nominal.textChangedListener {
            afterTextChanged {
                if (!nominal.text.toString().isEmpty()){
                    nominal_transaksi.text = formatRupiah.format(nominal.text.toString().toDouble())
                } else{
                    nominal_transaksi.text = "Rp0"
                }

            }
        }
        val calendar = Calendar.getInstance()
        val listener = DatePickerDialog.OnDateSetListener { datePicker, year, month, date ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, date)
            val day = listDay[calendar.get(Calendar.DAY_OF_WEEK)-1]
            val bulan = listMonth[datePicker.month]

            tanggal_transaksi.text = "$day, ${datePicker.dayOfMonth} $bulan ${datePicker.year}"
            tanggal.setText("$day, ${datePicker.dayOfMonth} $bulan ${datePicker.year}")
        }

        tanggal.showSoftInputOnFocus = false
        tanggal.onFocusChange { v, hasFocus ->
            if (hasFocus){
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                DatePickerDialog(ctx, listener,year,month,day).show()
            }
        }
        tanggal.onClick {

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)


            DatePickerDialog(ctx, listener,year,month,day).show()
        }

        pilih_tabungan.onClick {
            val view = LayoutInflater.from(ctx).inflate(R.layout.list_tabungan_dialog, null)
            val builder = AlertDialog.Builder(ctx)
                    .setView(view)
                    .setTitle("Pilih Tabungan")
            val dialog = builder.show()

            val lv:RecyclerView = view.find(R.id.rv_tabungan)

            val list = ctx.database.use {
                select(Tabungan.TABLE_TABUNGAN).parseList(classParser<Tabungan>())
            }
            listTabunganAdapter = ListTabunganAdapter(list){
                tabunganTerpilih = it.tabunganName
                nominalTabungan = it.tabunganNominal
                idTabungan = it.id
                tabungan_terpilih.text = tabunganTerpilih
                dialog.dismiss()
            }

            lv.layoutManager = LinearLayoutManager(ctx)
            lv.adapter = listTabunganAdapter

        }
    }

    override fun onBackClicked(callback: StepperLayout.OnBackClickedCallback?) {
        callback?.goToPrevStep()
    }

    override fun onSelected() {

    }

    override fun onCompleteClicked(callback: StepperLayout.OnCompleteClickedCallback?) {
        if (!judul.text.toString().isEmpty()&&!nominal.text.toString().isEmpty()&&!tanggal.text.toString().isEmpty()&&tabunganTerpilih!=""){
            if (arguments?.getString("JENIS_TRANSAKSI").toString()=="Pengeluaran"){
                if (nominalTabungan>=nominal.text.toString().toInt()){
                    ctx.database.use {
                        insert(
                                Transaksi.TABLE_TRANSAKSI,
                                Transaksi.TRANSAKSI_JUDUL to judul.text.toString(),
                                Transaksi.TRANSAKSI_JENIS to arguments?.getString("JENIS_TRANSAKSI").toString(),
                                Transaksi.TRANSAKSI_KATEGORI to kategori,
                                Transaksi.TRANSAKSI_NOMINAL to nominal.text.toString().toInt(),
                                Transaksi.TRANSAKSI_TANGGAL to tanggal.text.toString(),
                                Transaksi.TRANSAKSI_TABUNGAN to tabunganTerpilih
                        )
                    }
                    ctx.database.use {
                        val newNominal = nominalTabungan-nominal.text.toString().toInt()
                        update(Tabungan.TABLE_TABUNGAN, Tabungan.TABUNGAN_NOMINAL to newNominal).whereArgs("${Tabungan.ID} = {tabunganId}", "tabunganId" to idTabungan).exec()
                    }
                    toast("Berhasil ditambahkan")
                    callback?.complete()
                } else{
                    toast("Uang pada tabungan $tabunganTerpilih tidak cukup, silakan pilih tabungan yang lain")
                }
            } else if (arguments?.getString("JENIS_TRANSAKSI").toString()=="Pemasukan"){
                ctx.database.use {
                    insert(
                            Transaksi.TABLE_TRANSAKSI,
                            Transaksi.TRANSAKSI_JUDUL to judul.text.toString(),
                            Transaksi.TRANSAKSI_JENIS to arguments?.getString("JENIS_TRANSAKSI").toString(),
                            Transaksi.TRANSAKSI_KATEGORI to kategori,
                            Transaksi.TRANSAKSI_NOMINAL to nominal.text.toString().toInt(),
                            Transaksi.TRANSAKSI_TANGGAL to tanggal.text.toString(),
                            Transaksi.TRANSAKSI_TABUNGAN to tabunganTerpilih
                    )
                }

                ctx.database.use {
                    val newNominal = nominalTabungan+nominal.text.toString().toInt()
                    update(Tabungan.TABLE_TABUNGAN, Tabungan.TABUNGAN_NOMINAL to newNominal).whereArgs("${Tabungan.ID} = {tabunganId}", "tabunganId" to idTabungan).exec()
                }
                toast("Berhasil ditambahkan")
                callback?.complete()
            }
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