package com.sendinfo.wuzhizhou.utils

import com.blankj.utilcode.util.Utils
import com.sendinfo.honeywellprintlib.print.PrintServerUtil
import com.wellcom.GSdtUtil

/**
 * 这是一个单例类
 * 用来获取 打印机实例
 */
object HardwareExample {

    val whPrintServerUtil: PrintServerUtil by lazy { PrintServerUtil.getInstance(Utils.getApp()) }

    val tscPrintServerUtil: com.sendinfo.tscprintlib.PrintServerUtil by lazy {
        com.sendinfo.tscprintlib.PrintServerUtil.getInstance(Utils.getApp())
    }

    val bGSdtUtil: GSdtUtil by lazy { GSdtUtil.getInstance() }

}