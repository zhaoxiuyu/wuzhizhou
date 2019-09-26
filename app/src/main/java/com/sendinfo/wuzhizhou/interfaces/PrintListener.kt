package com.sendinfo.wuzhizhou.interfaces

import com.sendinfo.wuzhizhou.entitys.hardware.PrintStatus

interface PrintListener {

    fun printLinstener(printStatus: PrintStatus)

}