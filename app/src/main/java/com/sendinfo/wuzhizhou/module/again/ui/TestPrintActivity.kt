package com.sendinfo.wuzhizhou.module.again.ui

import com.base.library.mvp.BPresenter
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.entitys.hardware.PrintProgress
import com.sendinfo.wuzhizhou.entitys.hardware.PrintStatus
import com.sendinfo.wuzhizhou.interfaces.PrintListener
import com.sendinfo.wuzhizhou.interfaces.PrintStatusListener
import com.sendinfo.wuzhizhou.utils.defaultTemplate
import kotlinx.android.synthetic.main.activity_test_print.*

/**
 * 测试打印机
 */
class TestPrintActivity : BaseActivity<BPresenter>() {

    private val sb = StringBuilder("")

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_test_print)
    }

    override fun initData() {
        super.initData()

        butState.setOnClickListener {
            printStateOwner.getPrinterStatus(object : PrintStatusListener {
                override fun printLinstener(printStatus: PrintStatus) {
                    sb.appendln(printStatus.toString())
                    tvInfo.text = sb.toString()
                }
            })
        }
        butPrint.setOnClickListener {
            printStateOwner.printer(listOf(defaultTemplate()), object : PrintListener {
                override fun printBack(printProgress: PrintProgress?, errorMsg: String) {
                    sb.appendln(printProgress?.printReturnDto?.toString())
                    tvInfo.text = sb.toString()
                }
            })
        }
    }

}
