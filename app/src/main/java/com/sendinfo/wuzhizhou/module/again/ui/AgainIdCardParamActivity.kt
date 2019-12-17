package com.sendinfo.wuzhizhou.module.again.ui

import com.base.library.mvp.BPresenter
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.utils.getIdCard
import com.sendinfo.wuzhizhou.utils.putIdCard
import kotlinx.android.synthetic.main.activity_again_idcard_param.*
import kotlinx.android.synthetic.main.activity_base.*

/**
 * 设置身份证相关参数 activity_again_idcard_param
 */
class AgainIdCardParamActivity : BaseActivity<BPresenter>() {

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_again_idcard_param)
    }

    override fun initData() {
        super.initData()
        tts.startSurplus(120000)

        /**
         * 1 霍尼,2 HID ,身份证阅读器
         */
        if (getIdCard() == 1) {
            radioGroupIdCard.check(weier.id)
        } else {
            radioGroupIdCard.check(hid.id)
        }
        radioGroupIdCard.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == weier.id) {
                putIdCard(1)
            } else if (checkedId == hid.id) {
                putIdCard(2)
            }
        }

    }

}