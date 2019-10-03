package com.sendinfo.wuzhizhou.module.common.ui

import android.text.TextUtils
import android.view.View
import com.base.library.mvp.BPresenter
import com.blankj.utilcode.util.LogUtils
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.custom.InfoDialog
import com.sendinfo.wuzhizhou.custom.InfoDialog.*
import com.sendinfo.wuzhizhou.entitys.hardware.PrintStatus
import com.sendinfo.wuzhizhou.entitys.response.Notice
import com.sendinfo.wuzhizhou.interfaces.PrintStatusListener
import com.sendinfo.wuzhizhou.module.again.ui.AgainVerificationActivity
import com.sendinfo.wuzhizhou.module.common.presenter.MainPresenter
import com.sendinfo.wuzhizhou.module.purchase.contract.MainContract
import com.sendinfo.wuzhizhou.module.purchase.ui.PurchaseMainActivity
import com.sendinfo.wuzhizhou.module.take.ui.TakeMainActivity
import com.sendinfo.wuzhizhou.utils.getPrintNumber
import com.sendinfo.wuzhizhou.utils.startAct
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainPresenter>(), MainContract.View {
    private var infoDialog: InfoDialog? = null
    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_main)
        mPresenter = MainPresenter(this)
    }

    override fun initData() {
        super.initData()
        mPresenter?.queryNotice()
        tts.setDouble(View.OnClickListener { startAct(this, AgainVerificationActivity::class.java, isFinish = false) })
        fv.setDouble(View.OnClickListener { startAct(this, GestureActivity::class.java, isFinish = false) })
    }

    override fun onResume() {
        super.onResume()
        testing()
    }

    override fun queryNoticeSuccess(notice: Notice) {
        tvPurchase.setOnClickListener {
            if (TextUtils.isEmpty(notice.SaleNote))
                startAct(this, PurchaseMainActivity::class.java, isFinish = false)
            else
                showInfoDialog(
                    notice.SaleNote,
                    "购票须知",
                    View.OnClickListener {
                        infoDialog?.dismiss()
                        startAct(getContext(), PurchaseMainActivity::class.java, isFinish = false)
                    })
        }
        tvTake.setOnClickListener {
            if (TextUtils.isEmpty(notice.TakeNote))
                startAct(this, TakeMainActivity::class.java, isFinish = false)
            else
                showInfoDialog(
                    notice.TakeNote,
                    "取票须知",
                    View.OnClickListener {
                        infoDialog?.dismiss()
                        startAct(getContext(), TakeMainActivity::class.java, isFinish = false)
                    })
        }
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
        } else {
            //此时检测打印机是否正常
            printStateOwner.initPrinter(mApplication)
            printStateOwner.getPrinterStatus(object : PrintStatusListener {
                override fun printLinstener(printStatus: PrintStatus) {
                    if (!printStatus.succ) {
                        showDialog(
                            content = "打印机异常:${printStatus.msg}",
                            confirmListener = getConfirmFinishListener()
                        )
                        other("$printStatus", "检测打印机", "E")
                        LogUtils.i("打印机异常，$printStatus")
                    }
                }
            })
        }
    }

    private fun showInfoDialog(showInfo: String, title: String, listener: View.OnClickListener) {
        disInfoDialog()
        if (infoDialog == null) infoDialog = InfoDialog(this)
        infoDialog?.let {
            it.setConfirmClickListener(listener).setConfirmText("已阅读,同意").setContentText(showInfo).setTitleText(title)
        }
        infoDialog?.show()
    }

    private fun disInfoDialog() {
        infoDialog?.dismiss()
        infoDialog = null
    }
}
