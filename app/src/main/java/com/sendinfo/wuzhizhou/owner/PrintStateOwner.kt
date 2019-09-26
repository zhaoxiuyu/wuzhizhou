package com.sendinfo.wuzhizhou.owner

import androidx.lifecycle.LifecycleOwner
import com.base.library.interfaces.MyLifecycleObserver
import com.sendinfo.honeywellprintlib.print.PrintReturnCallback
import com.sendinfo.wuzhizhou.entitys.hardware.PrintStatus
import com.sendinfo.wuzhizhou.interfaces.PrintListener
import com.sendinfo.wuzhizhou.utils.HardwareExample
import com.sendinfo.wuzhizhou.utils.getDyj
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 根据打印机设置来获取打印机状态
 * 1 霍尼,2 TSC
 */
class PrintStateOwner : MyLifecycleObserver {

    private lateinit var owner: LifecycleOwner

    override fun onCreate(owner: LifecycleOwner) {
        this.owner = owner
    }

    override fun onDestroy(owner: LifecycleOwner) {
    }

    fun getPrintState(printListener: PrintListener) {
        if (getDyj() == 1) {
            getWhState(printListener)
        } else {
            getTSCState(printListener)
        }
    }

    private fun getWhState(printListener: PrintListener) {
        Observable.just("").map {
            val printReturnCallback = PrintReturnCallback { }
            HardwareExample.getWhPrint().getStatus(printReturnCallback)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner)))
            .subscribe({
                val printStatus = PrintStatus()
                printStatus.code = it.code
                printStatus.succ = it.isSucc
                printStatus.devicePreper = it.isDevicePreper
                printStatus.msg = it.msg
                printListener.printLinstener(printStatus)
            }, {
                it.printStackTrace()
            })
    }

    private fun getTSCState(printListener: PrintListener) {
        Observable.just("").map {
            val printReturnCallback = com.sendinfo.tscprintlib.PrintReturnCallback { }
            HardwareExample.getTscPrint().getStatus(printReturnCallback)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner)))
            .subscribe({
                val printStatus = PrintStatus()
                printStatus.code = it.code
                printStatus.succ = it.isSucc
                printStatus.devicePreper = it.isDevicePreper
                printStatus.msg = it.msg
                printListener.printLinstener(printStatus)
            }, {
                it.printStackTrace()
            })
    }

}