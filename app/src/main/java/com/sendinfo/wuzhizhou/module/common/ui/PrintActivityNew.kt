package com.sendinfo.wuzhizhou.module.common.ui

import android.content.Intent
import android.view.View
import com.base.library.mvp.BPresenter
import com.base.library.util.JsonUtils
import com.blankj.utilcode.util.LogUtils
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.entitys.hardware.PrintProgress
import com.sendinfo.wuzhizhou.entitys.response.PrintTempVo
import com.sendinfo.wuzhizhou.interfaces.PrintListener
import com.sendinfo.wuzhizhou.utils.getPrintNumber
import com.sendinfo.wuzhizhou.utils.putPrintNumber
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
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
        templateCheck()
    }

    private fun initTitle() {
        tts.startSurplus(8000 * totalPrint + 5000)
        tts.setBackVisibility(View.GONE) // 隐藏返回按钮
        tts.setBackOnClick(View.OnClickListener {
            showDialog(
                content = "出票完成会自动退出,你确定要手动退出吗?",
                confirmListener = getConfirmFinishListener(),
                isHideCancel = false
            )
        })
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
            other("errorChecking -  ${ex.printStackTrace()}", "$source 检查打印前状态", "E")
        }
        return false
    }

    /**
     * 模板替换
     */
    private fun templateCheck() {
        tvSum.text = "总票数：${totalPrint}张"
        tvCompleted.text = "已出票数：0张"

        Observable.just("").map {
            var printList = ArrayList<String>()
            printTemp.forEach {
                printList.add(rep(it.PrintTemp))
            }
            return@map printList
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
            .subscribe(Consumer {
                toPrint(it)
            }, Consumer {
                other("errorChecking -  ${it.printStackTrace()}", "$source 打印失败", "E")
                showDialog(
                    content = "打印失败，请联系管理员重打",
                    confirmListener = getConfirmFinishListener(),
                    isHideCancel = false
                )
            })
    }

    /**
     * 开始打印
     */
    private fun toPrint(printList: List<String>) {
        other("开始打印，总数量：$totalPrint，全部内容：${JsonUtils.toJson(printList)}", "$source toPrint", "I")
        printOwner.printer(printList) { printProgress ->
            LogUtils.i("打印进度：$printProgress")
            tvCompleted.text = "已出票数：  ${printProgress.progress}张"
            progressPrint = printProgress.progress
            if (!printProgress.succ) {
                tts.setBackVisibility(View.VISIBLE)
                tts.startSurplus(5000)
                showDialog(content = "${printProgress.errorMsg},请联系管理员重打", confirmListener = getConfirmFinishListener())
            }
            if (printProgress.isComplete) {
                tts.setBackVisibility(View.VISIBLE)
                tts.startSurplus(5000)
                tvInfo.text = "打印完成，请在出票口取票"
                other("出票完成：$printProgress", "$source 出票完成", "I")
                // 更新票数
                putPrintNumber(getPrintNumber() - printProgress.total)
                tts.updatePrintNumber()
            }
        }
    }

    /**
     * 模板内容超长截取
     */
    private fun rep(template: String): String {
        val sBuilder = StringBuilder()

        val datas = template.split("\n")
        // 拆分模板为每一行进行循环
        for (i in datas.indices) {
            val data = datas[i]
            // 拆分每一行的模板,用:进行分割,如果长度为5就说明符合规则,进行字符串截取
            val str = data.split(":")
            if (str.size == 5) {
                // 截取的开始位和结束位
                var start = 0
                var end = 0
                try {
                    start = Integer.valueOf(str[2])
                    end = Integer.valueOf(str[3])
                } catch (e: Exception) {
                }

                var rep = str[1]
                //开始截取位小于等 内容的长度
                if (start <= rep.length) {
                    if (end <= rep.length) {//结束位小于等 内容的长度,说明可以截取
                        rep = rep.substring(start, end)
                    } else {//结束位大于等 内容的长度,说明不在截取范围内,就显示剩余部分
                        rep = rep.substring(start, rep.length)
                    }
                } else {
                    rep = ""
                }

                val newStr = if (str[0].trim().length <= 5) {
                    str[0] + rep + str[4]
                } else {
                    str[0] + ":" + rep + str[4]
                }
                sBuilder.appendln(newStr)

            } else {
                sBuilder.appendln(data)
            }
        }
        LogUtils.d("最终模板如下 : ")
        LogUtils.d(sBuilder.toString())
        return sBuilder.toString()
    }

    override fun onStop() {
        super.onStop()
        lav.cancelAnimation()
    }

}
