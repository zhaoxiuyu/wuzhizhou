package com.sendinfo.wuzhizhou.module.purchase.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.entitys.response.GetTicketGroupVo

class PurchaseMainGroupAdapter :
    BaseQuickAdapter<GetTicketGroupVo, BaseViewHolder>(R.layout.item_purchase_main_group, null) {

    override fun convert(helper: BaseViewHolder, item: GetTicketGroupVo) {
        helper.setText(R.id.tvGroup, item.GroupName)
            .setChecked(R.id.tvGroup, item.isSelection)
            .addOnClickListener(R.id.tvGroup)
    }

}