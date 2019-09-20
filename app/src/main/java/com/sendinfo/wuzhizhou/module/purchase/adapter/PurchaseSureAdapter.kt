package com.sendinfo.wuzhizhou.module.purchase.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.entitys.response.GetTicketVo

class PurchaseSureAdapter : BaseQuickAdapter<GetTicketVo, BaseViewHolder>(R.layout.item_purchase_sure, null) {

    override fun convert(helper: BaseViewHolder, item: GetTicketVo) {
        helper.setText(R.id.tvTicketModelName, item.TicketModelName)
        helper.setText(R.id.tvTicketModelKindName, item.TicketModelKindName)
        helper.setText(R.id.tvRebatePrice, "${item.RebatePrice}")
        helper.setText(R.id.tvValiddays, "${item.Validdays}")
        helper.setText(R.id.tvNumber, "${item.tvNumber}")
        helper.addOnClickListener(R.id.btIdCard)
    }

}