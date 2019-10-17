package com.sendinfo.wuzhizhou.entitys.response

import java.io.Serializable

class GetTakeOrderInfoVo : Serializable {

    // 如果是1,取TakeOrderModels 订单
    // 否则 取TakeTicketModels 明细

    var IsOrder: String? = null

    var TakeOrderModels: MutableList<TakeOrderModelsVo>? = null

    var TakeTicketModels: MutableList<TakeTicketModelsVo>? = null

}