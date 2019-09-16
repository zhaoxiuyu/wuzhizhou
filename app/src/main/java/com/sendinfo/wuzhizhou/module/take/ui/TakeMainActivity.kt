package com.sendinfo.wuzhizhou.module.take.ui

import android.content.Intent
import com.base.library.entitys.BaseResponse
import com.base.library.mvp.BPresenter
import com.base.library.mvp.BaseView
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import kotlinx.android.synthetic.main.activity_take_main.*

class TakeMainActivity : BaseActivity<BPresenter>(), BaseView {

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_take_main)
    }

    override fun initData() {
        super.initData()

        qrCode.setOnClickListener { startAct(TakeQrCodeActivity::class.java) }
        number.setOnClickListener { startAct(TakeNumberActivity::class.java) }
        idCard.setOnClickListener { startAct(TakeIdCardActivity::class.java) }
        phone.setOnClickListener { startAct(TakePhoneActivity::class.java) }

    }

    private fun <T> startAct(cls: Class<T>) {
        val intent = Intent(this, cls)
        startActivity(intent)
        finish()
    }

    override fun bindData(baseResponse: BaseResponse) {
    }

    override fun bindError(string: String) {
    }

}
