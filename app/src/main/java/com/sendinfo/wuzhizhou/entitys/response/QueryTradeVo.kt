package com.sendinfo.wuzhizhou.entitys.response

class QueryTradeVo {

    var TradeId: String? = null // ": "string", -> 交易号
    var TicketModelCode: String? = null // ": "string",
    var TicketModelName: String? = null // ": "string", -> 票型名称
    var Barcode: String? = null // ": "string", -> 条码号
    var FirstBarcode: String? = null // ": "string", -> 条码号
    var LsNum: String? = null // ": "string", -> 支付流水
    var AssistCheckNo: String? = null // ": "string", -> 辅助吗
    var TelNo: String? = null // ": "string", -> 预订人手机号码
    var PayTypeName: String? = null // ": "string", -> 支付方式

    var IDCardInfo: IDCardInfo? = null
    var ParkInfos: MutableList<ParkInfos>? = null

}