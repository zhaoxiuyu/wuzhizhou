package com.sendinfo.wuzhizhou.module.again.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.entitys.response.QueryTradeVo

class ManagementRecordAdapter : BaseQuickAdapter<QueryTradeVo, BaseViewHolder>(R.layout.item_management_record, null) {

    override fun convert(helper: BaseViewHolder, item: QueryTradeVo) {
        helper.setText(R.id.tvTradeId, item.TradeId)
            .setText(R.id.tvTicketModelName, item.TicketModelName)
            .setText(R.id.tvParkName, item.ParkName)
            .setText(R.id.tvFirstBarcode, item.FirstBarcode)
            .setText(R.id.tvBarcode, item.Barcode)
            .setText(R.id.tvCheckFlag, item.CheckFlag)
            .addOnClickListener(R.id.butAgain)
    }

}