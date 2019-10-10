package com.sendinfo.wuzhizhou.module.common.ui

import android.content.Intent
import android.view.View
import com.base.library.mvp.BPresenter
import com.blankj.utilcode.util.LogUtils
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.entitys.hardware.PrintProgress
import com.sendinfo.wuzhizhou.entitys.hardware.PrintStatus
import com.sendinfo.wuzhizhou.entitys.response.PrintTempVo
import com.sendinfo.wuzhizhou.interfaces.PrintListener
import com.sendinfo.wuzhizhou.interfaces.PrintStatusListener
import com.sendinfo.wuzhizhou.utils.getPrintNumber
import com.sendinfo.wuzhizhou.utils.putPrintNumber
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_print.*

/**
 *  打印页面
 */
class PrintActivityNew : BaseActivity<BPresenter>() {

    private var totalPrint = 0
    private var progressPrint = 0
    var source: String = ""
    var printTemp: List<PrintTempVo> = ArrayList()

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
        soundPoolUtils.startPlayVideo(R.raw.chupiao)
        totalPrint = printTemp.size
        initTitle()
        if (errorChecking()) return
        toPrint()
    }

    private fun initTitle() {
        tts.startSurplus(8000 * totalPrint + 5000)
        try {
            if (totalPrint > 0) mHandler.postDelayed({ checkPrint() }, (8000 * totalPrint).toLong())
            tts.setBackVisibility(View.GONE) // 隐藏返回按钮
            tts.setBackOnClick(View.OnClickListener {
                showDialog(
                    content = "出票完成会自动退出,手动退出可能导致出票异常,你确定要手动退出吗?",
                    confirmListener = getConfirmFinishListener(),
                    isHideCancel = false
                )
            })
        } catch (ex: Exception) {
            ex.printStackTrace()
            other("$source errorChecking -  ${ex.printStackTrace()}", "初始化标题", "E")
        }
    }

    /**
     * 检测打印机是否正常
     * 已出票数 小于 总票数
     */
    private fun checkPrint() {
        if (progressPrint < totalPrint) {
            printStateOwner.getPrinterStatus(object : PrintStatusListener {
                override fun printLinstener(printStatus: PrintStatus) {
                    var errorMsg = "打印超时,还有${totalPrint - progressPrint}张未出票，请联系管理员重打"
                    showDialog(
                        content = errorMsg,
                        confirmListener = getConfirmFinishListener()
                    )
                    other("$source $errorMsg$printStatus", "检测打印机", "E")
                    LogUtils.i("$errorMsg$printStatus")
                }
            })
        }
    }

    // 验证票数和打印模板是否正常
    private fun errorChecking(): Boolean {
        try {
            var errorMsg: String? = null
            if (printTemp.isNullOrEmpty()) errorMsg = "打印内容为空，请联系管理员"
            if (getPrintNumber() < printTemp.size) {
                errorMsg = "可打印票数不足，请联系管理员"
            }
            errorMsg?.let {
                other(errorMsg, "$source 检查打印前状态", "E")
                showDialog(content = errorMsg, confirmListener = getConfirmFinishListener())
                return true
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            other("$source - errorChecking -  ${ex.printStackTrace()}", "检查打印前状态", "E")
        }
        return false
    }

    private fun toPrint() {
        try {
            tvSum.text = "总票数：${totalPrint}张"
            tvCompleted.text = "已出票数：0张"
            var printList = ArrayList<String>()
            printTemp.forEach { printList.add(it.PrintTemp) }
            printStateOwner.printer(printList, object : PrintListener {
                override fun printBack(printProgress: PrintProgress?, errorMsg: String) {
                    if (printProgress != null) {
                        LogUtils.i("打印进度：$printProgress")
                        other("打印进度：$printProgress", "打印进度", "I")
                        tvCompleted.text = "已出票数：  ${printProgress.progress}张"
                        progressPrint = printProgress.progress
                        if (!printProgress.succ) {
                            showDialog(
                                content = "${printProgress.errorMsg},请联系管理员重打",
                                confirmListener = getConfirmFinishListener()
                            )
                        }
                        if (printProgress.isComplete) {
                            tts.setBackVisibility(View.VISIBLE)
                            tts.startSurplus(8000)
                            tvInfo.text = "打印完成，请在出票口取票"
                            other("$source 出票完成：$printProgress", "出票完成", "I")
                            // 更新票数
                            putPrintNumber(getPrintNumber() - printProgress.total)
                            tts.updatePrintNumber()
                        }
                    } else {
                        other("$source 打印出错,进度：$progressPrint，错误信息：$errorMsg", "打印进度", "E")
                        showDialog(
                            content = "${printProgress?.errorMsg},请联系管理员重打",
                            confirmListener = getConfirmFinishListener()
                        )
                        LogUtils.i("打印出错,进度：$progressPrint，错误信息：$errorMsg")
                    }
                }
            })
        } catch (ex: Exception) {
            ex.printStackTrace()
            other("$source errorChecking -  ${ex.printStackTrace()}", "打印失败", "E")
            showDialog(
                content = "打印失败，请联系管理员重打",
                confirmListener = getConfirmFinishListener(),
                isHideCancel = false
            )
        }
    }

    override fun onStop() {
        super.onStop()
        lav.cancelAnimation()
    }

}
