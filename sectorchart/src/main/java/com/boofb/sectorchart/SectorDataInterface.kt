package com.boofb.sectorchart

interface SectorDataInterface {
    /**
     * 扇形标题
     */
    fun getSectorTitle(): String

    /**
     * 扇形值
     */
    fun getSectorValue(): Double

    /**
     * 颜色值
     */
    fun getSectorColor(): Int
}