package com.sendinfo.wuzhizhou.entitys.response

class QueryTradeVo {

    var TradeId: String? = null // ": "string", -> 交易号
    var TicketModelCode: String? = null // ": "string",
    var TicketModelName: String? = null // ": "string", -> 票型名称
    var Barcode: String? = null // ": "string", -> 条码号
    var FirstBarcode: String? = null // ": "string", -> 条码号
    var ParkName: String? = null // ": "string", -> 检票点
    var CheckFlag: String? = null // ": "string" -> 检票状态

}