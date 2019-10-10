package com.sendinfo.wuzhizhou.module.pay.ui

import android.animation.ValueAnimator
import android.content.Intent
import com.base.library.mvp.BPresenter
import com.base.library.util.isFastClick
import com.github.florent37.viewanimator.ViewAnimator
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.entitys.request.SaveOrderReq
import com.sendinfo.wuzhizhou.utils.startAct
import kotlinx.android.synthetic.main.activity_pay_type.*

/**
 * 支付方式选择
 */
class PayTypeActivity : BaseActivity<BPresenter>() {

    private lateinit var saveOrderVo: SaveOrderReq

    private var va: ViewAnimator? = null

    override fun initArgs(intent: Intent?) {
        super.initArgs(intent)
        intent?.let {
            saveOrderVo = it.getSerializableExtra("saveOrderVo") as SaveOrderReq
        }
    }

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_pay_type)
    }

    override fun initData() {
        super.initData()
        soundPoolUtils.startPlayVideo(R.raw.paytype)

        tvAli.setOnClickListener {
            if (isFastClick()) return@setOnClickListener

            saveOrderVo?.PayTypeCode = "18"
            saveOrderVo?.PayTypeName = "支付宝"

            startAct(this, Intent(this, PayActivity::class.java).putExtra("saveOrderVo", saveOrderVo))
        }
        tvWx.setOnClickListener {
            if (isFastClick()) return@setOnClickListener

            saveOrderVo?.PayTypeCode = "19"
            saveOrderVo?.PayTypeName = "微信"

            startAct(this, Intent(this, PayActivity::class.java).putExtra("saveOrderVo", saveOrderVo))
        }

        va = ViewAnimator.animate(tvAli, tvWx)
            .scale(1f, 0.9f)
            .duration(1000)
            .repeatMode(ViewAnimator.REVERSE)
            .repeatCount(ValueAnimator.INFINITE)
            .start()
    }

    override fun onDestroy() {
        super.onDestroy()
        va?.cancel()
    }

}
