package org.trydev.apps.dompetku.db

data class Tabungan (
        val id:Long,
        val tabunganName:String,
        val tabunganNominal:Int
){
    companion object {
        const val TABLE_TABUNGAN:String = "TABLE_TABUNGAN"
        const val ID:String = "ID_"
        const val TABUNGAN_NAME = "TABUNGAN_NAME"
        const val TABUNGAN_NOMINAL = "TABUNGAN_NOMINAL"
    }
}