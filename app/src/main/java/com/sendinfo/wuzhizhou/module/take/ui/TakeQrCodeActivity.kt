package com.sendinfo.wuzhizhou.module.take.ui

import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.entitys.response.TakeOrderModelsVo
import com.sendinfo.wuzhizhou.entitys.response.TakeTicketModelsVo
import com.sendinfo.wuzhizhou.module.take.contract.TakeOrderInfoContract
import com.sendinfo.wuzhizhou.module.take.presenter.TakeOrderInfoPresenter
import com.sendinfo.wuzhizhou.utils.TakeOrderInfo2
import com.sendinfo.wuzhizhou.utils.startActTakeDetailed
import com.sendinfo.wuzhizhou.utils.startActTakeOrder
import kotlinx.android.synthetic.main.activity_take_qrcode.*
import java.util.*

/**
 * 二维码
 */
class TakeQrCodeActivity : BaseActivity<TakeOrderInfoContract.Presenter>(), TakeOrderInfoContract.View {

    private val uuid: String by lazy { UUID.randomUUID().toString().replace("-", "") }

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_take_qrcode)
        mPresenter = TakeOrderInfoPresenter(this)
    }

    override fun initData() {
        super.initData()

        qca.startAnimation()

        // todo 两秒后根据二维码查询,现在为了方便测试,后面要改成二维码扫描
        mHandler.postDelayed({ getTakeOrderInfo("WT120181031201566171") }, 2000)

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

}
