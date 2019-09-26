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

/**
 * IP端口
 */
fun putIp(ip: String) {
    SPStaticUtils.put("ip", ip)
}

fun getIp(): String = SPStaticUtils.getString("ip")

/**
 * 获取二维码串口
 */
fun putQRcodeSerialPort(QRcodeSerialPort: String) {
    SPStaticUtils.put("QRcodeSerialPort", QRcodeSerialPort)
}

fun getQRcodeSerialPort(): String = SPStaticUtils.getString("QRcodeSerialPort")

/**
 * 指示灯控制板
 */
fun putIcCardSerialPort(IcCardSerialPort: String) {
    SPStaticUtils.put("IcCardSerialPort", IcCardSerialPort)
}

fun getIcCardSerialPort(): String = SPStaticUtils.getString("IcCardSerialPort")

