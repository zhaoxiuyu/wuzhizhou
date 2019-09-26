package com.sendinfo.wuzhizhou.module.again.ui

import com.base.library.util.isFastClick
import com.blankj.utilcode.util.StringUtils
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
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
            if (isFastClick()) return@setOnClickListener
            verify()
        }
    }

    private fun verify() {
        val code = tvOptorCode.text.toString()
        val pwd = tvOptorPwd.text.toString()

        if (StringUtils.isEmpty(code)) {
            YoYo.with(Techniques.Shake).playOn(tvOptorCode)
            return
        }
        if (StringUtils.isEmpty(pwd)) {
            YoYo.with(Techniques.Shake).playOn(tvOptorPwd)
            return
        }
        mPresenter?.login(code, pwd)
    }

    override fun loginSuccess(request: String?) {
        startAct(this, AgainMainActivity::class.java)
    }

    override fun loginError(msg: String?) {
        YoYo.with(Techniques.Shake).playOn(tvInfo)
        tvInfo.text = "$msg"
    }

}
