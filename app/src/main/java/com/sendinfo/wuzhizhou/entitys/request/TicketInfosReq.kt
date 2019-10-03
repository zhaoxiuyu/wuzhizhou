package com.sendinfo.wuzhizhou.entitys.request

import java.io.Serializable

class TicketInfosReq : Serializable {

    var BillDetailNo: String? = null // ": "string", -> 取票的订单明细号
    var TicketModelCode: String? = null // ": "string", -> 票的编码
    var TicketModelName: String? = null // ": "string", -> 票的名称
    var TicketModelPrice: Double? = null // ": 0, -> 票的单价
    var TicketCount: Int? = null // ": 0, -> 票型的数量

    var IDCards: MutableList<IDCardsReq>? = null // -> 实名制票对应的身份证

}