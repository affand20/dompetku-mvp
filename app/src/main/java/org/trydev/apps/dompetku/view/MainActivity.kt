package org.trydev.apps.dompetku.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import androidx.recyclerview.widget.RecyclerView
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.popup_main.*
import okhttp3.OkHttpClient
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk15.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.trydev.apps.dompetku.R
import org.trydev.apps.dompetku.R.id.*
import org.trydev.apps.dompetku.adapter.CardAdapter
import org.trydev.apps.dompetku.model.Tabungan

class MainActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Stetho.initializeWithDefaults(this)
        OkHttpClient().newBuilder().addNetworkInterceptor(StethoInterceptor()).build()

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,false)
        window.statusBarColor = Color.TRANSPARENT


        setContentView(R.layout.activity_main)

        bottom_app_bar.replaceMenu(R.menu.menu_navigation)
        loadDashboard(savedInstanceState)
        bottom_app_bar.setOnMenuItemClickListener {item ->
            when(item.itemId){
                dashboard -> {
//                    toast("Dashboard")
                    app_toolbar.title = "Dompetku"
                    loadDashboard(savedInstanceState)
                }
                target_tabungan -> {
//                    toast("Target Tabungan")
                    app_toolbar.title = "Tabungan"
                    loadTabungan(savedInstanceState)
                }
                laporan -> {
//                    toast("Laporan")
                    app_toolbar.title = "Laporan"
                    loadLaporan(savedInstanceState)
                }
                setting -> {
                    toast("Clicked")
                }
            }
            true
        }

        fab.onClick {
            showDialog()
        }
    }

    private fun showDialog(){
        val view = LayoutInflater.from(this).inflate(R.layout.popup_main, null)
        val builder = AlertDialog.Builder(this)
                .setView(view)
                .setTitle("Tambah Baru")
        val dialog = builder.show()

        val tambahKartu:ImageButton = view.find(R.id.tambah_kartu)
        val tambahPengeluaran:ImageButton = view.find(R.id.tambah_pengeluaran)
        val tambahPemasukan:ImageButton = view.find(R.id.tambah_pemasukan)
        val tambahTabungan:ImageButton = view.find(R.id.tambah_target_tabungan)
        tambahKartu.onClick {
            dialog.dismiss()
            tambahKartu()
        }
        tambahPengeluaran.onClick {
            dialog.dismiss()
            tambahPengeluaran()
        }
        tambahPemasukan.onClick {
            dialog.dismiss()
            tambahPemasukan()
        }
        tambahTabungan.onClick {
            dialog.dismiss()
            tambahTabungan()
        }

    }

    private fun tambahKartu(){
        startActivity<StepperActivity>("fragment" to "TAMBAH KARTU")
    }

    private fun tambahPengeluaran(){
        startActivity<StepperActivity>("fragment" to "TAMBAH PENGELUARAN")
    }

    private fun tambahPemasukan(){
        startActivity<StepperActivity>("fragment" to "TAMBAH PEMASUKAN")
    }

    private fun tambahTabungan(){
        startActivity<StepperActivity>("fragment" to "TAMBAH TARGET TABUNGAN")
    }

    private fun loadDashboard(savedInstanceState: Bundle?){
        if (savedInstanceState==null){
            supportFragmentManager.transaction {
                replace(R.id.layout_container, DashboardFragment())
            }
        }
    }

    private fun loadTabungan(savedInstanceState: Bundle?){
        if (savedInstanceState==null){
            supportFragmentManager.transaction {
                replace(R.id.layout_container, TabunganFragment())
            }
        }
    }

    private fun loadLaporan(savedInstanceState: Bundle?){
        if (savedInstanceState==null){
            supportFragmentManager.transaction {
                replace(R.id.layout_container, LaporanFragment())
            }
        }
    }

    private fun setWindowFlag(bits:Int, on:Boolean){
        val win = window
        val winParams = win.attributes
        if (on){
            winParams.flags = winParams.flags or bits
        } else{
            winParams.flags = winParams.flags and bits.inv()
        }
    }
}
