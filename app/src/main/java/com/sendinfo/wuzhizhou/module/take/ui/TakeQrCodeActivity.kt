package com.sendinfo.wuzhizhou.module.take.ui

import android.os.Handler
import android.os.Message
import com.blankj.utilcode.util.LogUtils
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.entitys.response.TakeOrderModelsVo
import com.sendinfo.wuzhizhou.entitys.response.TakeTicketModelsVo
import com.sendinfo.wuzhizhou.module.take.contract.TakeOrderInfoContract
import com.sendinfo.wuzhizhou.module.take.presenter.TakeOrderInfoPresenter
import com.sendinfo.wuzhizhou.owner.QrCodeOwner
import com.sendinfo.wuzhizhou.utils.TakeOrderInfo2
import com.sendinfo.wuzhizhou.utils.getQRcodeSerialPort
import com.sendinfo.wuzhizhou.utils.startActTakeDetailed
import com.sendinfo.wuzhizhou.utils.startActTakeOrder
import kotlinx.android.synthetic.main.activity_take_qrcode.*
import java.util.*

/**
 * 二维码
 */
class TakeQrCodeActivity : BaseActivity<TakeOrderInfoContract.Presenter>(), TakeOrderInfoContract.View {

    private val uuid: String by lazy { UUID.randomUUID().toString().replace("-", "") }
    private val qrCodeOwner: QrCodeOwner by lazy { QrCodeOwner(getQRcodeSerialPort(), qrCodeHandler) }

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_take_qrcode)
        mPresenter = TakeOrderInfoPresenter(this)
        lifecycle.addObserver(qrCodeOwner)
    }

    override fun initData() {
        super.initData()
        soundPoolUtils.startPlayVideo(R.raw.qrcode)
        qrCodeOwner.startReadSysCan()
    }

    private val qrCodeHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> LogUtils.d("扫描失败: ${msg.obj}")
                1 -> getTakeOrderInfo("${msg.obj}")
            }
        }
    }

    private fun getTakeOrderInfo(qrCode: String) {
        mPresenter?.getTakeOrderInfo(TakeOrderInfo2, qrCode, uuid)
    }

    override fun toOrder(takeOrderModels: MutableList<TakeOrderModelsVo>) {
        startActTakeOrder(this, takeOrderModels)
    }

    override fun toDetailed(takeTicketModels: MutableList<TakeTicketModelsVo>) {
        startActTakeDetailed(this, uuid, takeTicketModels)
    }

    override fun onDestroy() {
        super.onDestroy()
        lav.cancelAnimation()
    }

}
