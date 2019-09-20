package com.sendinfo.wuzhizhou.module.take.ui

import com.base.library.entitys.BaseResponse
import com.base.library.mvp.BPresenter
import com.base.library.mvp.BaseView
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.utils.startAct
import kotlinx.android.synthetic.main.activity_take_main.*

/**
 * 选择取票方式
 */
class TakeMainActivity : BaseActivity<BPresenter>(), BaseView {

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_take_main)
    }

    override fun initData() {
        super.initData()

        qrCode.setOnClickListener { startAct(this, TakeQrCodeActivity::class.java) }
        number.setOnClickListener { startAct(this, TakeNumberActivity::class.java) }
        idCard.setOnClickListener { startAct(this, TakeIdCardActivity::class.java) }

    }

    override fun bindData(baseResponse: BaseResponse) {
    }

    override fun bindError(string: String) {
    }

}
