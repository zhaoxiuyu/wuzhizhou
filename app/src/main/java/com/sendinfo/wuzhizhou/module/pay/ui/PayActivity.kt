package com.sendinfo.wuzhizhou.module.pay.ui

import android.content.Intent
import com.base.library.entitys.BaseResponse
import com.base.library.mvp.BPresenter
import com.base.library.mvp.BaseView
import com.base.library.util.glide.GlideApp
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import kotlinx.android.synthetic.main.activity_pay.*

/**
 * 支付页面
 */
class PayActivity : BaseActivity<BPresenter>(), BaseView {

    private var PayTypeCode = ""
    private var PayTypeName = ""

    override fun initArgs(intent: Intent?) {
        super.initArgs(intent)
        intent?.let {
            PayTypeCode = it.getStringExtra("PayTypeCode")
            PayTypeName = it.getStringExtra("PayTypeName")
        }
    }

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_pay)
    }

    override fun initData() {
        super.initData()
        // 18：支付宝 19：微信
        if (PayTypeCode == "18") {
            soundPoolUtils.startPlayVideo(R.raw.zhifubaopay)

            GlideApp.with(this).load(R.mipmap.zhifubaoyindaotu)
                .transition(DrawableTransitionOptions.withCrossFade()).into(ivGuide)
        } else {
            soundPoolUtils.startPlayVideo(R.raw.weixinpay)

            GlideApp.with(this).load(R.mipmap.weixinyindaotu)
                .transition(DrawableTransitionOptions.withCrossFade()).into(ivGuide)
        }
    }

    override fun bindData(baseResponse: BaseResponse) {
    }

    override fun bindError(string: String) {
    }

}
