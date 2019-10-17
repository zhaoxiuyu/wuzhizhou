package com.sendinfo.wuzhizhou.module.again.ui

import com.base.library.mvp.BPresenter
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.utils.getMid
import com.sendinfo.wuzhizhou.utils.getTid
import com.sendinfo.wuzhizhou.utils.putMid
import com.sendinfo.wuzhizhou.utils.putTid
import kotlinx.android.synthetic.main.activity_again_pay_param.*
import kotlinx.android.synthetic.main.activity_base.*

/**
 * 设置支付相关参数
 */
class AgainPayParamActivity : BaseActivity<BPresenter>() {

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_again_pay_param)
    }

    override fun initData() {
        super.initData()
        tts.startSurplus(120000)

        /**
         * 终端号
         */
        etTid.setText(getTid())
        butTid.setOnClickListener {
            val tid = etTid.text.toString()
            if (StringUtils.isEmpty(tid)) {
                ToastUtils.showShort("请输入终端号")
                return@setOnClickListener
            }
            putTid(tid)
            ToastUtils.showShort("保存成功")
        }

        /**
         * 商户号
         */
        etMid.setText(getMid())
        butMid.setOnClickListener {
            val mid = etMid.text.toString()
            if (StringUtils.isEmpty(mid)) {
                ToastUtils.showLong("请输入商户号")
                return@setOnClickListener
            }
            putMid(mid)
            ToastUtils.showLong("保存成功")
        }

    }

}