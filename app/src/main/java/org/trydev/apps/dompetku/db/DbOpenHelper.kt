package org.trydev.apps.dompetku.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class DbOpenHelper(ctx:Context):ManagedSQLiteOpenHelper(ctx, "Dompetku.db", null, 5) {
    companion object {
        private var instance:DbOpenHelper? = null

        @Synchronized
        fun getInstance(ctx:Context):DbOpenHelper{
            if (instance==null){
                instance = DbOpenHelper(ctx.applicationContext)
            }
            return instance as DbOpenHelper
        }
    }
    override fun onCreate(db: SQLiteDatabase) {

        db.createTable(Tabungan.TABLE_TABUNGAN, true,
                Tabungan.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                Tabungan.TABUNGAN_NAME to TEXT,
                Tabungan.TABUNGAN_NOMINAL to INTEGER)

        db.createTable(Transaksi.TABLE_TRANSAKSI, true,
                Transaksi.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                Transaksi.TRANSAKSI_JUDUL to TEXT,
                Transaksi.TRANSAKSI_JENIS to TEXT,
                Transaksi.TRANSAKSI_KATEGORI to TEXT,
                Transaksi.TRANSAKSI_NOMINAL to INTEGER,
                Transaksi.TRANSAKSI_TANGGAL to TEXT,
                Transaksi.TRANSAKSI_TABUNGAN to TEXT)

        db.createTable(TargetTabungan.TABLE_TARGET, true,
                TargetTabungan.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                TargetTabungan.TARGET_JUDUL to TEXT,
                TargetTabungan.TARGET_TOTAL to INTEGER,
                TargetTabungan.STATUS to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.dropTable(Tabungan.TABLE_TABUNGAN, true)
    }
}

// Access property for Context
val Context.database: DbOpenHelper
    get() = DbOpenHelper.getInstance(applicationContext)