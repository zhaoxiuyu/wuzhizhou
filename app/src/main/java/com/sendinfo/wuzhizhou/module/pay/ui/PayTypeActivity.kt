package com.sendinfo.wuzhizhou.module.pay.ui

import android.animation.ValueAnimator
import android.content.Intent
import com.base.library.mvp.BPresenter
import com.base.library.util.isFastClick
import com.github.florent37.viewanimator.ViewAnimator
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.utils.startAct
import kotlinx.android.synthetic.main.activity_pay_type.*

/**
 * 支付方式选择
 */
class PayTypeActivity : BaseActivity<BPresenter>() {

    private var va: ViewAnimator? = null

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_pay_type)
    }

    override fun initData() {
        super.initData()
        soundPoolUtils.startPlayVideo(R.raw.paytype)

        tvAli.setOnClickListener {
            if (isFastClick()) return@setOnClickListener

            startAct(
                this, Intent(this, PayActivity::class.java)
                    .putExtra("PayTypeCode", "18")
                    .putExtra("PayTypeName", "支付宝")
                    .putExtra("saveOrderVo", intent.getSerializableExtra("saveOrderVo"))
            )
        }
        tvWx.setOnClickListener {
            if (isFastClick()) return@setOnClickListener

            startAct(
                this, Intent(this, PayActivity::class.java)
                    .putExtra("PayTypeCode", "19")
                    .putExtra("PayTypeName", "微信")
                    .putExtra("saveOrderVo", intent.getSerializableExtra("saveOrderVo"))
            )
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
