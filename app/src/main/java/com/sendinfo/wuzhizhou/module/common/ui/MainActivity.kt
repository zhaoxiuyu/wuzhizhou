package com.sendinfo.wuzhizhou.module.common.ui

import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.custom.InfoDialog
import com.sendinfo.wuzhizhou.entitys.hardware.PrintStatus
import com.sendinfo.wuzhizhou.entitys.response.Notice
import com.sendinfo.wuzhizhou.interfaces.PrintStatusListener
import com.sendinfo.wuzhizhou.module.again.ui.AgainVerificationActivity
import com.sendinfo.wuzhizhou.module.common.presenter.MainPresenter
import com.sendinfo.wuzhizhou.module.purchase.contract.MainContract
import com.sendinfo.wuzhizhou.module.purchase.ui.PurchaseMainActivity
import com.sendinfo.wuzhizhou.module.take.ui.TakeMainActivity
import com.sendinfo.wuzhizhou.utils.getMid
import com.sendinfo.wuzhizhou.utils.getPrintNumber
import com.sendinfo.wuzhizhou.utils.getTid
import com.sendinfo.wuzhizhou.utils.startAct
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainPresenter>(), MainContract.View {

    private var infoDialog: InfoDialog? = null
    private var notice: Notice? = null

    override fun initArgs(intent: Intent?) {
        super.initArgs(intent)
        intent?.let {
            notice = it.getSerializableExtra("notice") as Notice?
        }
    }

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_main)
    }

    override fun initData() {
        super.initData()
        tts.setDouble(View.OnClickListener { startAct(this, AgainVerificationActivity::class.java, isFinish = false) })

        tvPurchase.setOnClickListener {
            soundPoolUtils.startPlayVideo(R.raw.yueduxuzhi)

            if (TextUtils.isEmpty(notice?.SaleNote))
                startAct(this, PurchaseMainActivity::class.java, isFinish = false)
            else
                showInfoDialog(
                    notice?.SaleNote ?: "",
                    "购票须知",
                    View.OnClickListener {
                        infoDialog?.dismiss()
                        startAct(getContext(), PurchaseMainActivity::class.java, isFinish = false)
                    })
        }
        tvTake.setOnClickListener {
            soundPoolUtils.startPlayVideo(R.raw.yueduxuzhi)

            if (TextUtils.isEmpty(notice?.TakeNote))
                startAct(this, TakeMainActivity::class.java, isFinish = false)
            else
                showInfoDialog(
                    notice?.TakeNote ?: "",
                    "取票须知",
                    View.OnClickListener {
                        infoDialog?.dismiss()
                        startAct(getContext(), TakeMainActivity::class.java, isFinish = false)
                    })
        }
    }

    override fun onResume() {
        super.onResume()
        testing()
    }

    override fun queryNoticeSuccess(notice: Notice) {
    }

    /**
     * 检测
     */
    private fun testing() {
        //打印纸票数不足,请管理员重新设置
        if (getPrintNumber() < 1) {
            soundPoolUtils.startPlayVideo(R.raw.piaozhibuzu)

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
        } else if (StringUtils.isEmpty(getTid()) || StringUtils.isEmpty(getMid())) {
            showDialog(
                content = "终端编号 或者 商户号为空", confirmBtnText = "去设置",
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
            printOwner.getPrinterStatus {
                if (!it.succ) {
                    showDialog(
                        content = "打印机异常:${it.msg}",
                        confirmListener = getConfirmFinishListener()
                    )
                    other("$it", "检测打印机", "E")
                    LogUtils.i("打印机异常，$it")
                }
            }
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
