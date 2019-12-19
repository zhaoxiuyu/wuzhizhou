package com.sendinfo.wuzhizhou.owner

import android.text.TextUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.base.library.interfaces.MyLifecycleObserver
import com.blankj.utilcode.util.LogUtils
import com.sendinfo.wuzhizhou.entitys.hardware.PrintProgress
import com.sendinfo.wuzhizhou.entitys.hardware.PrintStatus
import com.sendinfo.wuzhizhou.utils.HardwareExample
import com.sendinfo.wuzhizhou.utils.getDyj
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * 打印工具类
 * 1 霍尼,2 TSC
 */
class PrintOwner : MyLifecycleObserver {

    private var isInterrupt = false

    private lateinit var owner: LifecycleOwner

    override fun onCreate(owner: LifecycleOwner) {
        this.owner = owner
    }

    override fun onDestroy(owner: LifecycleOwner) {
        endPrinter()
    }

    fun getPrinterStatus(listenerBlock: (PrintStatus) -> Unit) {
        Observable
            .create<PrintStatus> {
                val dyj = getDyj()//1维尔打印机 ; 2 TSC打印机
                val printStatus = PrintStatus()
                if (dyj == 1) {
                    val printReturnDto = HardwareExample.whPrintServerUtil.getStatus(null)
                    printStatus.msg = printReturnDto.msg ?: "打印机故障"
                    printStatus.code = printReturnDto.code
                    printStatus.succ = printReturnDto.isSucc
                    printStatus.devicePreper = printReturnDto.isDevicePreper
                } else {
                    val printReturnDto = HardwareExample.tscPrintServerUtil.getStatus(null)
                    printStatus.msg = printReturnDto.msg ?: "打印机故障"
                    printStatus.code = printReturnDto.code
                    printStatus.succ = printReturnDto.isSucc
                    printStatus.devicePreper = printReturnDto.isDevicePreper
                }
                it.onNext(printStatus)
                it.onComplete()
            }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, Lifecycle.Event.ON_STOP)))
            .subscribe({ listenerBlock(it) }, { LogUtils.e("获取打印机状态保存 ${it.message}") })
    }

    fun printer(list: List<String>, listenerBlock: (PrintProgress) -> Unit) {
        Observable
            .create<PrintProgress> {
                val dyj = getDyj()//1维尔打印机 ; 2 TSC打印机
                val printProgress = PrintProgress()
                if (dyj == 1) {
                    welPrint(list, printProgress, it)
                } else {
                    tscPrint(list, printProgress, it)
                }
                if (isInterrupt || printProgress.progress == printProgress.total) {
                    it.onComplete()
                }
            }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, Lifecycle.Event.ON_DESTROY)))
            .subscribe({ listenerBlock.invoke(it) }, {
                LogUtils.e("打印报错 ${it.message}")
                val printProgress = PrintProgress()
                printProgress.errorMsg = "打印报错 ${it.message}"
                listenerBlock.invoke(printProgress)
            })
    }

    private fun welPrint(list: List<String>, printProgress: PrintProgress, observer: ObservableEmitter<PrintProgress>) {
        printProgress.succ = true
        printProgress.total = list.size
        for (i in 1..list.size) {
            if (isInterrupt) {
                printProgress.succ = false
                printProgress.errorMsg = "打印中断"
                observer.onNext(printProgress)
                break
            }
            printProgress.temp = list[i - 1] + "\n\n"
            val dto = HardwareExample.whPrintServerUtil.print(list[i - 1] + "\n\n", { }, { }, 15000, true)//打印
            try {
                Thread.sleep(500)
            } catch (ex: Exception) {
            }
            printProgress.printReturnDto = dto
            if (dto.isDevicePreper && dto.isSucc && dto.isNoErrorHappen) {
                printProgress.succ = true
                printProgress.progress = i
            } else {
                val msg = dto.msg
                if (!TextUtils.isEmpty(msg)) {
                    if (dto.msg.contains("介质") || dto.msg.contains("标签") || dto.msg.contains("超时")) {
                        printProgress.succ = false
                        printProgress.errorMsg = msg
                    }
                }
                if (!dto.isNoErrorHappen || !dto.isSucc || !dto.isDevicePreper) {
                    printProgress.succ = false
                    printProgress.errorMsg = dto.msg
                }
                observer.onNext(printProgress)
                break
            }
            printProgress.isComplete = (printProgress.progress == printProgress.total)
            observer.onNext(printProgress)
        }
    }

    private fun tscPrint(list: List<String>, printProgress: PrintProgress, observer: ObservableEmitter<PrintProgress>) {
        printProgress.succ = true
        printProgress.total = list.size
        for (i in 1..list.size) {
            if (isInterrupt) {
                printProgress.succ = false
                printProgress.errorMsg = "打印中断"
                observer.onNext(printProgress)
                break
            }
            val stringList = ArrayList<String>()
            stringList.add(list[i - 1])
            printProgress.temp = "$stringList"
            val dto = HardwareExample.tscPrintServerUtil.print(stringList, null, 10000, null, true)
            try {
                Thread.sleep(1000)
            } catch (ex: Exception) {
            }
            printProgress.tscPrintReturnDto = dto
            if (dto.isDevicePreper && dto.isSucc && dto.isNoErrorHappen) {
                printProgress.succ = true
                printProgress.progress = i
            } else {
                val msg = dto.msg
                if (!TextUtils.isEmpty(msg)) {
                    if (dto.msg.contains("介质") || dto.msg.contains("标签") || dto.msg.contains("超时")) {
                        printProgress.succ = false
                        printProgress.errorMsg = msg
                    }
                }
                if (!dto.isNoErrorHappen || !dto.isSucc || !dto.isDevicePreper) {
                    printProgress.succ = false
                    printProgress.errorMsg = dto.msg
                }
                observer.onNext(printProgress)
                break
            }
            printProgress.isComplete = (printProgress.progress == printProgress.total)
            observer.onNext(printProgress)
        }
    }

    private fun endPrinter() {
        isInterrupt = true
    }
}