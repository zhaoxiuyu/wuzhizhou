package com.sendinfo.wuzhizhou.module.again.ui

import android.content.Intent
import android.os.SystemClock
import com.base.library.util.isFastClick
import com.blankj.utilcode.util.StringUtils
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.sendinfo.wuzhizhou.BuildConfig
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.module.again.contract.AgainVerificationContract
import com.sendinfo.wuzhizhou.module.again.presenter.AgainVerificationPresenter
import com.sendinfo.wuzhizhou.module.common.ui.GestureActivity
import com.sendinfo.wuzhizhou.utils.home
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
        if (BuildConfig.DEBUG) {
            tvOptorCode.setText("ADMIN")
            tvOptorPwd.setText("1")
        }
        setDouble()
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

    /**
     * 设置三击事件 start ====================================================
     */
    private val mHits = LongArray(3)

    fun setDouble() {
        tvInfo.setOnClickListener {
            System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
            mHits[mHits.size - 1] = SystemClock.uptimeMillis() //获取手机开机时间
            if (mHits[mHits.size - 1] - mHits[0] < 500) {
                /**双击的业务逻辑 */
                val intent = Intent(this, GestureActivity::class.java)
                intent.putExtra("source", home)
                startAct(this, intent)
            }
        }
    }

}
