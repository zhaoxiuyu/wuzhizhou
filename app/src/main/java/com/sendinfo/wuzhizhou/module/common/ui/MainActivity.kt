package com.sendinfo.wuzhizhou.module.common.ui

import android.view.View
import com.base.library.mvp.BPresenter
import com.blankj.utilcode.util.LogUtils
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.module.again.ui.AgainVerificationActivity
import com.sendinfo.wuzhizhou.module.purchase.ui.PurchaseMainActivity
import com.sendinfo.wuzhizhou.module.take.ui.TakeMainActivity
import com.sendinfo.wuzhizhou.utils.getPrintNumber
import com.sendinfo.wuzhizhou.utils.startAct
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<BPresenter>() {

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_main)
    }

    override fun initData() {
        super.initData()
        tvPurchase.setOnClickListener { startAct(this, PurchaseMainActivity::class.java, isFinish = false) }
        tvTake.setOnClickListener { startAct(this, TakeMainActivity::class.java, isFinish = false) }

        tts.setDouble(View.OnClickListener {
            startAct(this, AgainVerificationActivity::class.java, isFinish = false)
        })
    }

    override fun onResume() {
        super.onResume()
        testing()
    }

    /**
     * 检测
     */
    private fun testing() {
        //打印纸票数不足,请管理员重新设置
        if (getPrintNumber() < 1) {
            showDialog(
                content = "打印纸票数不足,请管理员重新设置", confirmBtnText = "去设置",
                confirmListener = OnConfirmListener {
                    xPopup?.dismissWith {
                        LogUtils.d("onDismiss")
                        startAct(this@MainActivity, AgainVerificationActivity::class.java)
                    }
                },
                cancelListener = getCancelFinishListener(),
                isHideCancel = false
            )
            return
        }
    }

}
