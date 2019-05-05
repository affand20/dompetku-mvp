package org.trydev.apps.dompetku.db

data class Transaksi (
        val id:Long,
        val judul:String,
        val jenis:String,
        val kategori:String,
        val nominal:Int,
        val tanggal:String,
        val tabungan:String
){
    companion object {
        const val TABLE_TRANSAKSI = "TABLE_TRANSAKSI"
        const val ID = "ID_"
        const val TRANSAKSI_JUDUL = "JUDUL"
        const val TRANSAKSI_JENIS = "JENIS"
        const val TRANSAKSI_NOMINAL = "NOMINAL"
        const val TRANSAKSI_KATEGORI = "KATEGORI"
        const val TRANSAKSI_TANGGAL = "TANGGAL"
        const val TRANSAKSI_TABUNGAN = "TABUNGAN"
    }
}
