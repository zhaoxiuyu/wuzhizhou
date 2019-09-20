package com.sendinfo.wuzhizhou.module.take.ui

import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.entitys.response.TakeOrderModelsVo
import com.sendinfo.wuzhizhou.entitys.response.TakeTicketModelsVo
import com.sendinfo.wuzhizhou.module.take.contract.TakeOrderInfoContract
import com.sendinfo.wuzhizhou.module.take.presenter.TakeOrderInfoPresenter
import com.sendinfo.wuzhizhou.utils.TakeOrderInfo1
import com.sendinfo.wuzhizhou.utils.startActTakeDetailed
import com.sendinfo.wuzhizhou.utils.startActTakeOrder
import kotlinx.android.synthetic.main.activity_take_idcard.*
import java.util.*

/**
 * 身份证
 */
class TakeIdCardActivity : BaseActivity<TakeOrderInfoContract.Presenter>(), TakeOrderInfoContract.View {

    private val uuid: String by lazy { UUID.randomUUID().toString().replace("-", "") }

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_take_idcard)
        mPresenter = TakeOrderInfoPresenter(this)
    }

    override fun initData() {
        super.initData()
        ica.startAnimation()

        // todo 两秒后根据身份证查询,现在为了方便测试,后面要改成身份证阅读器
        mHandler.postDelayed({ getTakeOrderInfo("330326199304202419") }, 2000)

    }

    private fun getTakeOrderInfo(idCard: String) {
        mPresenter?.getTakeOrderInfo(TakeOrderInfo1, idCard, uuid)
    }

    override fun toOrder(takeOrderModels: MutableList<TakeOrderModelsVo>) {
        startActTakeOrder(this, takeOrderModels)
    }

    override fun toDetailed(takeTicketModels: MutableList<TakeTicketModelsVo>) {
        startActTakeDetailed(this, uuid, takeTicketModels)
    }

}
