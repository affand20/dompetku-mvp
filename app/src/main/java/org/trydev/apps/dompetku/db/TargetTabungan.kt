package org.trydev.apps.dompetku.db

data class TargetTabungan (
        val id:Long,
        val judulTarget:String,
        val totalTarget:Int,
        val status:String
){
    companion object {
        const val TABLE_TARGET= "TABLE_TARGET_TABUNGAN"
        const val ID = "ID_"
        const val TARGET_JUDUL = "JUDUL"
        const val TARGET_TOTAL = "TOTAL"
        const val STATUS = "TERCAPAI"
    }
}