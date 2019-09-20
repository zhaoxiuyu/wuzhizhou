package com.sendinfo.wuzhizhou.module.take.ui

import com.base.library.util.isFastClick
import com.blankj.utilcode.util.StringUtils
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.entitys.response.TakeOrderModelsVo
import com.sendinfo.wuzhizhou.entitys.response.TakeTicketModelsVo
import com.sendinfo.wuzhizhou.module.take.contract.TakeOrderInfoContract
import com.sendinfo.wuzhizhou.module.take.presenter.TakeOrderInfoPresenter
import com.sendinfo.wuzhizhou.utils.TakeOrderInfo3
import com.sendinfo.wuzhizhou.utils.startActTakeDetailed
import com.sendinfo.wuzhizhou.utils.startActTakeOrder
import kotlinx.android.synthetic.main.activity_take_number.*
import java.util.*

/**
 * 辅助吗
 */
class TakeNumberActivity : BaseActivity<TakeOrderInfoContract.Presenter>(), TakeOrderInfoContract.View {

    private val uuid: String by lazy { UUID.randomUUID().toString().replace("-", "") }

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_take_number)
        mPresenter = TakeOrderInfoPresenter(this)
    }

    override fun initData() {
        super.initData()
        butVerify.setOnClickListener { getTakeOrderInfo() }
    }

    private fun getTakeOrderInfo() {
        if (isFastClick()) return

        val number = etIdCard.text.toString()
        if (StringUtils.isEmpty(number)) {
            tvInfo.text = "请输入辅助码"
        } else {
            mPresenter?.getTakeOrderInfo(TakeOrderInfo3, number, uuid, false)
        }
    }

    override fun toOrder(takeOrderModels: MutableList<TakeOrderModelsVo>) {
        startActTakeOrder(this, takeOrderModels)
    }

    override fun toDetailed(takeTicketModels: MutableList<TakeTicketModelsVo>) {
        startActTakeDetailed(this, uuid, takeTicketModels)
    }

}
