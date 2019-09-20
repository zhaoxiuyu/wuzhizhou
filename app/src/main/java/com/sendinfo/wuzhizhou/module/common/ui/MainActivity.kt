package com.sendinfo.wuzhizhou.module.common.ui

import com.base.library.mvp.BPresenter
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.module.purchase.ui.PurchaseMainActivity
import com.sendinfo.wuzhizhou.module.take.ui.TakeMainActivity
import com.sendinfo.wuzhizhou.utils.startAct
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<BPresenter>() {

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_main)
    }

    override fun initData() {
        super.initData()
        tvPurchase.setOnClickListener { startAct(this, PurchaseMainActivity::class.java) }
        tvTake.setOnClickListener { startAct(this, TakeMainActivity::class.java) }
    }

}
