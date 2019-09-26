package com.sendinfo.wuzhizhou.entitys.hardware

class PrintStatus {

    var devicePreper: Boolean = false
    var succ: Boolean = false
    var code: String? = null
    var msg: String? = null

    override fun toString(): String {
        return "PrintStatus(devicePreper=$devicePreper, succ=$succ, code=$code, msg=$msg)"
    }

}