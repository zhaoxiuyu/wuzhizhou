package com.sendinfo.wuzhizhou.owner

import android.annotation.SuppressLint
import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import com.base.library.interfaces.MyLifecycleObserver
import com.sendinfo.wuzhizhou.entitys.hardware.PrintProgress
import com.sendinfo.wuzhizhou.entitys.hardware.PrintStatus
import com.sendinfo.wuzhizhou.interfaces.PrintListener
import com.sendinfo.wuzhizhou.interfaces.PrintStatusListener
import com.sendinfo.wuzhizhou.utils.HardwareExample
import com.sendinfo.wuzhizhou.utils.getDyj
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

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
        endPrinter()
    }

    private var disposablePrinterStatus: Disposable? = null
    private var disposablePrint: Disposable? = null
    private var isInterrupt = false
    private var printListener: PrintListener? = null
    private var statusListener: PrintStatusListener? = null

    @SuppressLint("CheckResult")
    fun getPrinterStatus(listener: PrintStatusListener) {
        if (disposablePrinterStatus != null && !disposablePrinterStatus!!.isDisposed) {
            disposablePrinterStatus!!.dispose()
            disposablePrinterStatus = null
        }
        statusListener = listener
        Observable
            .unsafeCreate { observer: Observer<in Any> ->
                val dyj = getDyj()//1维尔打印机 ; 2 TSC打印机
                val printStatus: PrintStatus
                printStatus = if (dyj == 1) {
                    getWelStatus()
                } else {
                    getTscStatus()
                }
                observer.onNext(printStatus)
                observer.onComplete()
            }
            .subscribeOn(Schedulers.newThread())
            .doOnSubscribe { disposable: Disposable -> disposablePrinterStatus = disposable }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { ret ->
                if (ret is PrintStatus && statusListener != null) {
                    statusListener?.printLinstener(ret)
                }
            }
    }

    private fun getTscStatus(): PrintStatus {
        val printStatus = PrintStatus()
        val printReturnDto = HardwareExample.tscPrintServerUtil?.getStatus(null)
        if (TextUtils.isEmpty(printReturnDto.msg)) {
            printStatus.msg = "打印机故障"
        } else {
            printStatus.msg = printReturnDto.msg
        }
        printStatus.code = printReturnDto.code
        printStatus.succ = printReturnDto.isSucc
        printStatus.devicePreper = printReturnDto.isDevicePreper
        return printStatus
    }

    private fun getWelStatus(): PrintStatus {
        val printStatus = PrintStatus()
        val printReturnDto = HardwareExample.whPrintServerUtil?.getStatus(null)
        if (TextUtils.isEmpty(printReturnDto.msg)) {
            printStatus.msg = "打印机故障"
        } else {
            printStatus.msg = printReturnDto.msg
        }
        printStatus.code = printReturnDto.code
        printStatus.succ = printReturnDto.isSucc
        printStatus.devicePreper = printReturnDto.isDevicePreper
        return printStatus
    }

    @SuppressLint("CheckResult")
    fun printer(list: List<String>, listener: PrintListener?) {
        this.printListener = listener
        if (disposablePrint != null && !disposablePrint!!.isDisposed) {
            disposablePrint?.dispose()
            disposablePrint = null
            isInterrupt = false
        }
        Observable
            .unsafeCreate { observer: Observer<in Any> ->
                val dyj = getDyj()//1维尔打印机 ; 2 TSC打印机
                val printProgress = PrintProgress()
                if (dyj == 1) {
                    welPrint(list, printProgress, observer)
                } else {
                    tscPrint(list, printProgress, observer)
                }
                if (isInterrupt || printProgress.progress === printProgress.total) {
                    observer.onComplete()
                }
            }
            .subscribeOn(Schedulers.newThread())
            .doOnSubscribe { disposable: Disposable -> disposablePrint = disposable }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ ret ->
                if (ret is PrintProgress && printListener != null) printListener!!.printBack(
                    ret,
                    ""
                )
            }, { throwable -> printListener!!.printBack(null, throwable.toString()) })
    }

    private fun welPrint(
        list: List<String>,
        printProgress: PrintProgress,
        observer: Observer<in Any>
    ) {
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
            val dto = HardwareExample.whPrintServerUtil?.print(
                list[i - 1] + "\n\n",
                { },
                { },
                15000,
                true
            )//打印
            try {
                Thread.sleep(500)
            } catch (ex: Exception) {
            }

            printProgress.printReturnDto = dto
            if (dto.isDevicePreper && dto.isSucc && dto.isNoErrorHappen) {
                printProgress.succ = true
                printProgress.progress = i
            } else {
                if (!dto.isDevicePreper) {
                    printProgress.succ = false
                    printProgress.errorMsg = "打印机设备未打开,devicePreper:" + dto.isDevicePreper
                }
                if (!dto.isSucc) {
                    printProgress.succ = false
                    printProgress.errorMsg = dto.msg
                }
                if (!dto.isNoErrorHappen) {
                    printProgress.succ = false
                    printProgress.errorMsg = dto.msg + " noErrorHappen:" + dto.isNoErrorHappen
                }
                val msg = dto.msg
                if (!TextUtils.isEmpty(msg)) {
                    if (dto.msg.contains("介质") || dto.msg.contains("标签") || dto.msg.contains("超时")) {
                        printProgress.succ = false
                        printProgress.errorMsg = msg
                    }
                }
                observer.onNext(printProgress)
                break
            }
            if (printProgress.progress === printProgress.total) {
                printProgress.isComplete = true
            }
            observer.onNext(printProgress)
        }
    }

    private fun tscPrint(
        list: List<String>,
        printProgress: PrintProgress,
        observer: Observer<in Any>
    ) {
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
            val dto = HardwareExample.tscPrintServerUtil?.print(stringList, null, 10000, null, true)
            try {
                Thread.sleep(1000)
            } catch (ex: Exception) {
            }

            printProgress.tscPrintReturnDto = dto
            if (dto.isDevicePreper && dto.isSucc && dto.isNoErrorHappen) {
                printProgress.succ = true
                printProgress.progress = i
            } else {
                if (!dto.isDevicePreper) {
                    printProgress.succ = false
                    printProgress.errorMsg = "打印机设备未打开,devicePreper:" + dto.isDevicePreper
                }
                if (!dto.isSucc) {
                    printProgress.succ = false
                    printProgress.errorMsg = dto.msg
                }
                if (!dto.isNoErrorHappen) {
                    printProgress.succ = false
                    printProgress.errorMsg = dto.msg + " noErrorHappen:" + dto.isNoErrorHappen
                }
                val msg = dto.msg
                if (!TextUtils.isEmpty(msg)) {
                    if (dto.msg.contains("介质") || dto.msg.contains("标签") || dto.msg.contains("超时")) {
                        printProgress.succ = false
                        printProgress.errorMsg = dto.msg
                    }
                }
                observer.onNext(printProgress)
                break
            }
            if (printProgress.progress === printProgress.total) {
                printProgress.isComplete = true
            }
            observer.onNext(printProgress)
        }
    }

    private fun endPrinter() {
        if (disposablePrint != null && !disposablePrint!!.isDisposed) {
            disposablePrint?.dispose()
            disposablePrint = null
        }
        if (disposablePrinterStatus != null && !disposablePrinterStatus!!.isDisposed) {
            disposablePrinterStatus!!.dispose()
            disposablePrinterStatus = null
        }
        statusListener = null
        printListener = null
        isInterrupt = true
    }
}