package com.sendinfo.wuzhizhou.interfaces

import com.sendinfo.wuzhizhou.entitys.hardware.PrintStatus

interface PrintStatusListener {

    fun printLinstener(printStatus: PrintStatus)

}