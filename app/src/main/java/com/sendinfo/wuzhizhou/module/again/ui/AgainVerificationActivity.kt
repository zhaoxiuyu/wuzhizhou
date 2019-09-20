package com.sendinfo.wuzhizhou.module.again.ui

import android.view.View
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.module.again.contract.AgainVerificationContract
import com.sendinfo.wuzhizhou.module.again.presenter.AgainVerificationPresenter
import com.sendinfo.wuzhizhou.utils.startAct
import kotlinx.android.synthetic.main.activity_again_verification.*

/**
 * 验证管理员
 */
class AgainVerificationActivity : BaseActivity<AgainVerificationContract.Presenter>(),
    AgainVerificationContract.View {

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_again_verification)
        mPresenter = AgainVerificationPresenter(this)
    }

    override fun initData() {
        super.initData()
        butVerify.setOnClickListener {
            mPresenter?.check("")
        }
    }

    override fun checkSuccess(request: String?) {
        startAct(this, AgainMainActivity::class.java)
    }

    override fun checkError(msg: String?) {
        tvInfo.visibility = View.VISIBLE
        tvInfo.text = "$msg"
    }

}
