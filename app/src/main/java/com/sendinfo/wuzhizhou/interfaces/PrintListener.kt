package com.sendinfo.wuzhizhou.interfaces

import com.sendinfo.wuzhizhou.entitys.hardware.PrintProgress

/**
 * 备注：打印进度
 */
interface PrintListener {
    fun printBack(printProgress: PrintProgress?, errorMsg: String)
}
