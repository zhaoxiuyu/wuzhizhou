package com.sendinfo.wuzhizhou.module.take.adapter

import com.base.library.entitys.BaseResponse
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sendinfo.wuzhizhou.R

class TakeOrderAdapter : BaseQuickAdapter<BaseResponse, BaseViewHolder>(R.layout.item_take_order, null) {

    override fun convert(helper: BaseViewHolder, item: BaseResponse) {
//        helper.setText(R.id.orderNo, item.orderNo)
//        helper.setText(R.id.routeName, item.routeName)
//        helper.setText(R.id.planBeginDate, item.planBeginDate.substring(0, 10))
//        helper.setText(R.id.amount, item.amount)
//        helper.addOnClickListener(R.id.btSubmit)
    }

}