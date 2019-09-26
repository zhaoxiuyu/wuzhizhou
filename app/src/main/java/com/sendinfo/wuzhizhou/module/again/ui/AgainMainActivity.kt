package com.sendinfo.wuzhizhou.module.again.ui

import com.base.library.mvp.BPresenter
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.utils.startAct
import kotlinx.android.synthetic.main.activity_again_main.*

/**
 * 管理员首页
 */
class AgainMainActivity : BaseActivity<BPresenter>() {

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_again_main)
    }

    override fun initData() {
        super.initData()

        wham.setOnClickListener { startAct(this, AgainRecordActivity::class.java, isFinish = false) }

        setting.setOnClickListener { startAct(this, AgainSettingActivity::class.java, isFinish = false) }

    }

}
