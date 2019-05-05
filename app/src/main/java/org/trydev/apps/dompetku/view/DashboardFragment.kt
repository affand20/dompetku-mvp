package org.trydev.apps.dompetku.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_tambah_kartu.*
import kotlinx.android.synthetic.main.fragment_tambah_transaksi.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.db.*
import org.jetbrains.anko.find
import org.jetbrains.anko.info
import org.jetbrains.anko.sdk15.coroutines.onClick
import org.jetbrains.anko.sdk15.coroutines.onFocusChange
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.toast
import org.trydev.apps.dompetku.R
import org.trydev.apps.dompetku.adapter.CardAdapter
import org.trydev.apps.dompetku.adapter.KategoriAdapter
import org.trydev.apps.dompetku.adapter.ListTabunganAdapter
import org.trydev.apps.dompetku.adapter.TransaksiAdapter
import org.trydev.apps.dompetku.db.Tabungan
import org.trydev.apps.dompetku.db.database
import org.trydev.apps.dompetku.db.Transaksi
import org.trydev.apps.dompetku.model.Kategori
import org.trydev.apps.dompetku.presenter.MainPresenter
import java.text.NumberFormat
import java.util.*

class DashboardFragment:Fragment(), MainView, AnkoLogger {
    //    private lateinit var cardList: RecyclerVie

    private lateinit var presenter: MainPresenter

    private var tabungan:MutableList<Tabungan> = mutableListOf()

    private var transaksi:MutableList<Transaksi> = mutableListOf()
    private lateinit var cardAdapter: CardAdapter

    private lateinit var transaksiAdapter: TransaksiAdapter

    val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in","ID"))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        app_toolbar?.title = "Dompetku"



        cardAdapter = CardAdapter(tabungan){tabungan ->
            val view = LayoutInflater.from(ctx).inflate(R.layout.option_dialog, null)
            val builder = AlertDialog.Builder(ctx)
                    .setView(view)
            val dialog = builder.show()

            val edit:TextView = view.find(R.id.action_edit)
            val hapus:TextView = view.find(R.id.action_hapus)


            edit.onClick {
                val viewEditor = LayoutInflater.from(ctx).inflate(R.layout.fragment_tambah_kartu, null)
                val builderEditor = AlertDialog.Builder(ctx)
                        .setView(viewEditor)
                dialog.dismiss()
                val dialogEditor = builderEditor.show()

                val titleDialog = viewEditor.find<TextView>(R.id.title_dialog)
                val card = viewEditor.find<CardView>(R.id.card)
                val namaKartu = viewEditor.find<EditText>(R.id.nama_kartu)
                val nominalKartu = viewEditor.find<EditText>(R.id.nominal_kartu)
                val submit = viewEditor.find<Button>(R.id.submit)

                card.visibility = View.GONE
                submit.visibility = View.VISIBLE
                titleDialog.text = "Edit Tabungan"
                namaKartu.setText(tabungan.tabunganName)
                nominalKartu.setText(tabungan.tabunganNominal.toString())

                submit.onClick {
                    if (!namaKartu.text.isEmpty() && !nominalKartu.text.isEmpty()){
                        info("NAMA KARTU ${namaKartu.text}")
                        ctx.database.use {
                            update(Tabungan.TABLE_TABUNGAN, Tabungan.TABUNGAN_NAME to namaKartu.text.toString(), Tabungan.TABUNGAN_NOMINAL to nominalKartu.text.toString().toInt()).whereArgs("${Tabungan.ID} = {tabunganId}", "tabunganId" to tabungan.id).exec()
                        }
                        toast("Edit berhasil")
                        dialogEditor.dismiss()
                        presenter.getAllCard()
                    } else{
                        toast("Mohon lengkapi semua input")
                    }
                }
            }

            hapus.onClick {
                ctx.database.use {
                    delete(Tabungan.TABLE_TABUNGAN, "${Tabungan.ID} = {tabunganId}", "tabunganId" to tabungan.id)
                }
                dialog.dismiss()
                presenter.getAllCard()
            }
        }
        card_list.adapter = cardAdapter

        transaksi_list.layoutManager = LinearLayoutManager(ctx)
        transaksiAdapter = TransaksiAdapter(transaksi){transaksi ->
            val view = LayoutInflater.from(ctx).inflate(R.layout.option_dialog, null)
            val builder = AlertDialog.Builder(ctx)
                    .setView(view)
            val dialog = builder.show()

            val edit:TextView = view.find(R.id.action_edit)
            edit.visibility = View.GONE
            val hapus:TextView = view.find(R.id.action_hapus)


            /*edit.onClick {
                val viewEditor = LayoutInflater.from(ctx).inflate(R.layout.fragment_tambah_transaksi, null)
                val builderEditor = AlertDialog.Builder(ctx).setView(viewEditor)
                val dialogEditor = builderEditor.show()
                dialog.dismiss()

                val cardView:CardView = viewEditor.find(R.id.card)
                val submit:Button = viewEditor.find(R.id.submit)
                val pilihTabungan:Button = viewEditor.find(R.id.pilih_tabungan)
                val title:TextView = viewEditor.find(R.id.title_transaksi)
                val tabunganTerpilih:TextView = viewEditor.find(R.id.tabungan_terpilih)
                val kategoriTerpilih:TextView = viewEditor.find(R.id.kategori_terpilih)
                val listKategori:RecyclerView = viewEditor.find(R.id.list_kategori)
                val judul:EditText = viewEditor.find(R.id.judul)
                val nominal:EditText = viewEditor.find(R.id.nominal)
                val tanggal:EditText = viewEditor.find(R.id.tanggal)
                val kategoriAdapter = KategoriAdapter(listOf(Kategori("Makan",R.drawable.burger), Kategori("Transport",R.drawable.transport), Kategori("Belanja",R.drawable.shopping), Kategori("Lain-lain",R.drawable.uncategorized))){
                    kategoriTerpilih.text = it.nama
                }
                val listDay = arrayOf("Minggu","Senin","Selasa","Rabu","Kamis","Jum'at","Sabtu")
                val listMonth = arrayOf("Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember")
                var nominalTabungan = 0
                var idTabungan:Long = 0

                if (transaksi.jenis=="Pengeluaran"){
                    title.text = "Edit Pengeluaran"
                } else{
                    title.text = "Edit Pemasukan"
                }
                cardView.visibility = View.GONE
                submit.visibility = View.VISIBLE
                kategoriTerpilih.visibility = View.VISIBLE
                listKategori.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
                listKategori.adapter = kategoriAdapter
                judul.setText(transaksi.judul)
                nominal.setText(transaksi.nominal.toString())
                tanggal.setText(transaksi.tanggal)
                kategoriTerpilih.text = transaksi.kategori
                tabunganTerpilih.text = transaksi.tabungan

                pilihTabungan.onClick {
                    val viewTabungan = LayoutInflater.from(ctx).inflate(R.layout.list_tabungan_dialog, null)
                    val builderTabungan = AlertDialog.Builder(ctx)
                            .setView(viewTabungan)
                            .setTitle("Pilih Tabungan")
                    val dialogTabungan = builderTabungan.show()

                    val lv:RecyclerView = viewTabungan.find(R.id.rv_tabungan)

                    val listTabunganAdapter = ListTabunganAdapter(tabungan){
                        tabunganTerpilih.text = it.tabunganName
                        nominalTabungan = it.tabunganNominal
                        idTabungan = it.id

                        dialogTabungan.dismiss()
                    }

                    lv.layoutManager = LinearLayoutManager(ctx)
                    lv.adapter = listTabunganAdapter

                }

                val calendar = Calendar.getInstance()
                val listener = DatePickerDialog.OnDateSetListener { datePicker, year, month, date ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, date)
                    val day = listDay[calendar.get(Calendar.DAY_OF_WEEK)-1]
                    val bulan = listMonth[datePicker.month]
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

                submit.onClick {
                    if (!judul.text.toString().isEmpty()&&!nominal.text.toString().isEmpty()&&!tanggal.text.toString().isEmpty()){
                        if (transaksi.jenis=="Pengeluaran"){
                            if (nominalTabungan>=nominal.text.toString().toInt()){
                                ctx.database.use {
                                    update(
                                            Transaksi.TABLE_TRANSAKSI,
                                            Transaksi.TRANSAKSI_JUDUL to judul.text.toString(),
                                            Transaksi.TRANSAKSI_KATEGORI to kategoriTerpilih.text.toString(),
                                            Transaksi.TRANSAKSI_NOMINAL to nominal.text.toString().toInt(),
                                            Transaksi.TRANSAKSI_TANGGAL to tanggal.text.toString(),
                                            Transaksi.TRANSAKSI_TABUNGAN to tabunganTerpilih.text.toString()
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
                            dialogEditor.dismiss()
                        }
                    } else{
                        toast("Mohon lengkapi semua input")
                    }
                }

            }*/

            hapus.onClick {
                ctx.database.use {
                    delete(Transaksi.TABLE_TRANSAKSI, "${Transaksi.ID} = {transaksiId}", "transaksiId" to transaksi.id)
                }
                dialog.dismiss()
                presenter.getLastTransaksi()
            }
        }
        transaksi_list.adapter = transaksiAdapter

        presenter = MainPresenter(this, ctx)
        presenter.getAllCard()
        presenter.getLastTransaksi()
        presenter.getPemasukanHarian()
        presenter.getPengeluaranHarian()
    }

    override fun onResume() {
        super.onResume()
        presenter.getAllCard()
        presenter.getLastTransaksi()
        presenter.getPemasukanHarian()
        presenter.getPengeluaranHarian()
    }

    override fun showCardList(card: List<Tabungan>?) {
        card?.let{
            tabungan.clear()
            tabungan.addAll(it)
            cardAdapter.notifyDataSetChanged()
        }
    }

    override fun showTransaksi(data: List<Transaksi>?) {
        data?.let {
            transaksi.clear()
            transaksi.addAll(it)
            transaksiAdapter.notifyDataSetChanged()
        }
    }

    override fun showTotalUang(total: Int) {
        total_uang.text = formatRupiah.format(total.toDouble())
    }

    override fun showPemasukanHarian(total: Int) {
        pemasukan_harian.text = formatRupiah.format(total.toDouble())
    }

    override fun showPengeluaranHarian(total: Int) {
        pengeluaran_harian.text = formatRupiah.format(total.toDouble())
    }
}
