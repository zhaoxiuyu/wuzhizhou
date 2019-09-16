package com.sendinfo.wuzhizhou.utils

import com.blankj.utilcode.util.SPStaticUtils

/**
 * 设备编码
 */
fun putShebeiCode(shebeiCode: String) {
    SPStaticUtils.put("shebeiCode", shebeiCode)
}

fun getShebeiCode(): String = SPStaticUtils.getString("shebeiCode")

/**
 * 可出票数
 */
fun putPrintNumber(printNumber: Int) {
    SPStaticUtils.put("printNumber", printNumber)
}

fun getPrintNumber(): Int = SPStaticUtils.getInt("printNumber", 0)

/**
 * 选择身份证设备,1 维尔,2 HID
 */
fun putIdCard(idCard: Int) {
    SPStaticUtils.put("idCard", idCard)
}

fun getIdCard(): Int = SPStaticUtils.getInt("idCard", 1)

/**
 * 选择打印机,1 霍尼,2 TSC
 */
fun putDyj(dyj: Int) {
    SPStaticUtils.put("dyj", dyj)
}

fun getDyj(): Int = SPStaticUtils.getInt("dyj", 1)
