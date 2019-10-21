package com.sendinfo.wuzhizhou.module.purchase.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.entitys.request.IDCardsReq

class RealNameAdapter :
    BaseQuickAdapter<IDCardsReq, BaseViewHolder>(R.layout.item_real_name, null) {

    override fun convert(helper: BaseViewHolder, item: IDCardsReq) {
        helper.setText(R.id.tvName, item.FName)
            .setText(R.id.tvIdCard, "${StringBuilder(item.FNumber).replace(6, 12, "******")}")
            .setText(R.id.tvSex, item.FSex)
            .addOnClickListener(R.id.butRemove)
    }

}