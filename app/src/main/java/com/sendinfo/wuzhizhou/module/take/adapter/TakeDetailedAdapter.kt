package com.sendinfo.wuzhizhou.module.take.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.entitys.response.TakeTicketModelsVo

class TakeDetailedAdapter : BaseQuickAdapter<TakeTicketModelsVo, BaseViewHolder>(R.layout.item_take_detailed, null) {

    override fun convert(helper: BaseViewHolder, item: TakeTicketModelsVo) {
        helper.setText(R.id.tvTicketmodelName, item.TicketmodelName)
            .setText(R.id.tvTicketmodelKindName, item.TicketmodelKindName)
            .setText(R.id.tvTicketPrice, "${item.TicketPrice}")
            .setText(R.id.tvTicketNum, "${item.TicketNum}")
            .setText(R.id.tvTotalNum, "${item.TotalNum}")
            .setText(R.id.tvTakeNum, "${item.TakeNum}")
            .setText(R.id.tvNumber, "${item.number}")
            .addOnClickListener(R.id.tvRemove)
            .addOnClickListener(R.id.tvAdd)
    }

}