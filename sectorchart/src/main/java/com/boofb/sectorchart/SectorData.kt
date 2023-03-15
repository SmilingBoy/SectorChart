package com.boofb.sectorchart

data class SectorData(var title: String, var v: Double, var color: Int) : SectorDataInterface {
    override fun getSectorTitle(): String {
        return title
    }

    override fun getSectorValue(): Double {
        return v
    }

    override fun getSectorColor(): Int {
        return color
    }
}