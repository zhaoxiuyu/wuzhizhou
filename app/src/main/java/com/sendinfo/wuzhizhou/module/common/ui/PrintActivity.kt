package com.sendinfo.wuzhizhou.module.common.ui

import android.content.Intent
import com.base.library.mvp.BPresenter
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity

/**
 * 取票 购票 重打 打印机出票页面
 */
class PrintActivity : BaseActivity<BPresenter>() {

    var printTemp: MutableList<String>? = null

    override fun initArgs(intent: Intent?) {
        super.initArgs(intent)
        intent?.let {
            printTemp = it.getSerializableExtra("printTemp") as MutableList<String>
        }
    }

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_print)
    }

    override fun initData() {
        super.initData()
    }

}
