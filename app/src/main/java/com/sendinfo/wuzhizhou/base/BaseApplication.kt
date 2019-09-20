package com.sendinfo.wuzhizhou.base

import com.base.library.base.BApplication
import com.sendinfo.wuzhizhou.utils.putIp
import com.sendinfo.wuzhizhou.utils.putPrintNumber
import com.sendinfo.wuzhizhou.utils.putShebeiCode

class BaseApplication : BApplication() {

    override fun onCreate() {
        super.onCreate()

        //todo 这里为了方便测试,ip端口和设备编号是写死的
        putIp("http://192.168.200.236:9010/")
        putShebeiCode("1001")
        putPrintNumber(1000)
    }

}