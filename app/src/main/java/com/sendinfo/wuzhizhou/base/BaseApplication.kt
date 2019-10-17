package com.sendinfo.wuzhizhou.base

import com.base.library.base.BApplication
import com.base.library.util.glide.GlideDiskClean
import com.base.library.util.glide.GlideMemoryClean
import com.blankj.utilcode.util.LogUtils
import com.sendinfo.devicehelp.service.devicehelp.SdFileUtil
import com.sendinfo.wuzhizhou.BuildConfig
import com.sendinfo.wuzhizhou.utils.putIcCardSerialPort
import com.sendinfo.wuzhizhou.utils.putQRcodeSerialPort
import com.sendinfo.wuzhizhou.utils.putShebeiCode
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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