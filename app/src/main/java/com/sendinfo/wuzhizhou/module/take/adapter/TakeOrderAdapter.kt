package com.sendinfo.wuzhizhou.module.take.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.entitys.response.TakeOrderModelsVo

class TakeOrderAdapter : BaseQuickAdapter<TakeOrderModelsVo, BaseViewHolder>(R.layout.item_take_order, null) {

    override fun convert(helper: BaseViewHolder, item: TakeOrderModelsVo) {
        helper.setText(R.id.tvBillNo, item.BillNo)
            .setText(R.id.tvTravelDate, item.TravelDate)
            .setText(R.id.tvUserName, item.UserName)
            .addOnClickListener(R.id.btSubmit)
    }

}