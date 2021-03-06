package com.sendinfo.wuzhizhou.base

import com.base.library.base.BApplication
import com.sendinfo.devicehelp.service.devicehelp.SdFileUtil
import com.sendinfo.wuzhizhou.BuildConfig
import com.sendinfo.wuzhizhou.utils.HardwareExample
import com.sendinfo.wuzhizhou.utils.putIcCardSerialPort
import com.sendinfo.wuzhizhou.utils.putQRcodeSerialPort
import com.sendinfo.wuzhizhou.utils.putShebeiCode

class BaseApplication : BApplication() {

    override fun onCreate() {
        super.onCreate()

        // 设备号
        var device_Sn = SdFileUtil.getDeviceArgs("Device_Sn")
        // 指示灯
        val P52_IcCardSerialPort = SdFileUtil.getDeviceArgs("P52_IcCardSerialPort")
        // 二维码串口
        val P02_QRcodeSerialPort = SdFileUtil.getDeviceArgs("P02_QRcodeSerialPort")

        putQRcodeSerialPort(P02_QRcodeSerialPort)
        putIcCardSerialPort(P52_IcCardSerialPort)
        putShebeiCode(device_Sn)

        HardwareExample.whPrintServerUtil
        HardwareExample.tscPrintServerUtil
        HardwareExample.bGSdtUtil

//        Observable.create<String> {
//            GlideDiskClean()
//        }.subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                GlideMemoryClean()
//                LogUtils.d("清除缓存")
//            }

        //方便测试
        if (BuildConfig.DEBUG) {
//            putIp("http://192.168.200.64:9010/")
//            putPrintNumber(1000)
//            putShebeiCode("38798F897AF6F39E")
//            putIdCard(1)
        }

    }

}