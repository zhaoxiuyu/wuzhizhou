package com.sendinfo.wuzhizhou.entitys.request

class SaveOrderReq {

    /**
     * bilno   billdetailno  lockguid   都是取票特有 ,购票忽略
     */
    var OptType: String? = null // ": "string", -> 0:售票  1：取票
    var TerminalCode: String? = null // ": "string", -> 设备编码
    var PayTypeCode: String? = null // ": "string", -> 支付方式 售票（05：银行卡 18：支付宝 19：微信）取票（07：电子商务）
    var PayTypeName: String? = null // ": "string",
    var PaySum: Double? = null // ": 0, -> 总金额
    var TotalTicketCount: Int? = null // ": 0, -> 总票数
    var BillNo: String? = null // ": "string", -> 取票的订单号
    var LockGuid: String? = null // ": "string", -> 查询明细的uuid

    var TicketInfos: MutableList<TicketInfosReq>? = null // 票型

}