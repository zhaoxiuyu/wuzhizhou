package com.sendinfo.wuzhizhou.utils

import com.blankj.utilcode.util.Utils
import com.sendinfo.honeywellprintlib.print.PrintServerUtil
import com.wellcom.GSdtUtil

/**
 * 这是一个单例类
 * 用来获取 打印机实例
 */
object HardwareExample {

    private val whPrintServerUtil: PrintServerUtil = PrintServerUtil.getInstance(Utils.getApp())

    private val tscPrintServerUtil: com.sendinfo.tscprintlib.PrintServerUtil =
        com.sendinfo.tscprintlib.PrintServerUtil.getInstance(Utils.getApp())

    private val bGSdtUtil: GSdtUtil = GSdtUtil.getInstance()

    fun getWhPrint(): PrintServerUtil = whPrintServerUtil

    fun getTscPrint(): com.sendinfo.tscprintlib.PrintServerUtil = tscPrintServerUtil

    fun getGSdtUtil(): GSdtUtil = bGSdtUtil

}