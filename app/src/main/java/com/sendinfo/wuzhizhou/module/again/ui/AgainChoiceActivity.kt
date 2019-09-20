package com.sendinfo.wuzhizhou.module.again.ui

import com.base.library.mvp.BPresenter
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.utils.startAct
import kotlinx.android.synthetic.main.activity_again_choice.*

/**
 * 选择重打方式
 */
class AgainChoiceActivity : BaseActivity<BPresenter>() {

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_again_choice)
    }

    override fun initData() {
        super.initData()

        // 交易记录
        record.setOnClickListener { startAct(this, AgainRecordActivity::class.java) }

        // 辅助吗
        fuzhuma.setOnClickListener { }

        // 身份证
        idCard.setOnClickListener { }
    }

}
