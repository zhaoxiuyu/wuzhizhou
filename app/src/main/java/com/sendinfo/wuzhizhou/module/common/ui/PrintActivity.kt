package com.sendinfo.wuzhizhou.module.common.ui

import android.content.Intent
import android.view.View
import com.base.library.mvp.BPresenter
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.sendinfo.honeywellprintlib.print.PrintReturnCallback
import com.sendinfo.honeywellprintlib.print.PrintServerUtil
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.entitys.response.PrintTempVo
import com.sendinfo.wuzhizhou.utils.HardwareExample
import com.sendinfo.wuzhizhou.utils.getPrintNumber
import com.sendinfo.wuzhizhou.utils.putPrintNumber
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_print.*

/**
 * 取票 购票 重打 打印机出票页面
 */
class PrintActivity : BaseActivity<BPresenter>() {

    var source: String? = null
    var printTemp: MutableList<PrintTempVo>? = null

    var sum = 0 // 总票数
    var completedSum = 0 // 已出票数

    override fun initArgs(intent: Intent?) {
        super.initArgs(intent)
        intent?.let {
            source = it.getStringExtra("source")
            printTemp = it.getSerializableExtra("printTemp") as MutableList<PrintTempVo>
        }
    }

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_print)
    }

    override fun initData() {
        super.initData()

        sum = printTemp?.size ?: 0
        tvSum.text = "总张数 : $sum"
        tvCompleted.text = "已出票 : $completedSum"

        tts.setBackVisibility(View.GONE) // 隐藏返回按钮
        tts.setBackOnClick(View.OnClickListener {
            showDialog(
                content = "出票完成会自动退出,手动退出可能导致出票异常,你确定要手动退出吗?",
                confirmListener = getConfirmFinishListener(),
                isHideCancel = false
            )
        })

        print()
    }

    private fun print() {
        RxJavaPlugins.setErrorHandler {
            LogUtils.e(it.message ?: "RxJavaError")
        }
        Observable.just(printTemp?.get(completedSum)?.PrintTemp).map {
            LogUtils.d("发送打印指令 : \n $it")
            other("$it", "$source 发送打印指令", "I")
            val printReturnCallback = PrintReturnCallback {}
            PrintServerUtil.getInstance(applicationContext).print("$it", printReturnCallback, 15000)
//            HardwareExample.getWhPrint().print("$it", printReturnCallback, 15000)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
            .subscribe({
                other("$it", "$source 打印机返回状态", "I")
                tvInfo.text = "${it.msg}" // 这里显示打印状态
                if (it.isSucc) {
                    // 减去票数
                    var printNumber = getPrintNumber()
                    printNumber -= 1
                    putPrintNumber(printNumber)
                    tts.updatePrintNumber()

                    // 增加已出票数
                    completedSum += 1
                    tvCompleted.text = "已出票 : $completedSum"

                    if (it.isNoErrorHappen) {
                        if (completedSum >= sum) { // 打印数量大于等于总数量,完成打印
                            tts.setBackVisibility(View.VISIBLE)
                        } else {
                            print() // 再次打印
                        }
                    } else {
                        tts.setBackVisibility(View.VISIBLE)
                    }
                } else {
                    tts.setBackVisibility(View.VISIBLE)
                }
            }, {
                tts.setBackVisibility(View.VISIBLE)
                other("${it.localizedMessage}", "$source 打印机返回状态Error", "E")
                it.printStackTrace()
            })
    }

    override fun onStop() {
        super.onStop()
        lav.cancelAnimation()
    }

}
