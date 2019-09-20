package com.sendinfo.wuzhizhou.module.take.contract

import com.base.library.mvp.BPresenter
import com.base.library.mvp.BView
import com.sendinfo.wuzhizhou.entitys.response.TakeOrderModelsVo
import com.sendinfo.wuzhizhou.entitys.response.TakeTicketModelsVo

/**
 * 用于身份证 辅助吗 二维码 查询取票订单 明细
 */
class TakeOrderInfoContract {

    interface View : BView {
        fun toOrder(takeOrderModels: MutableList<TakeOrderModelsVo>)
        fun toDetailed(takeTicketModels: MutableList<TakeTicketModelsVo>)
    }

    interface Presenter : BPresenter {
        fun getTakeOrderInfo(takeFlag: String, keyword: String, lockGuid: String, finish: Boolean = true)
    }

}