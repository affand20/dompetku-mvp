package org.trydev.apps.dompetku.model

data class Transaksi (
        private var id:Int? = null,

        var judul:String? = null,

        var jenis:String? = null,

        var nominal:Int? = null,

        var kategori:String? = null,

        var tanggal:String? = null

)