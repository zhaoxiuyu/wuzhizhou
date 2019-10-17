package com.sendinfo.wuzhizhou.entitys.response

import java.io.Serializable

class TakeTicketModelsVo : Serializable {

    var BillNo: String? = null // ": "string",
    var BillDetailNo: String? = null // ": "string",
    var BillDate: String? = null // ": "2019-09-19T05:37:25.944Z",
    var InvaliDatetime: String? = null // ": "2019-09-19T05:37:25.944Z",
    var TicketNum: Int = 0 // ": 0, -> 可取票数
    var TotalNum: Int = 0 // ": 0, -> 总票数
    var TakeNum: Int = 0 // ": 0, -> 已取票数
    var TicketPrice: Double = 0.0 // ": 0, -> 票价
    var SellPrice: Double = 0.0 // ": 0, -> 销售价格
    var PayFlag: String? = null // ": "string",
    var PayType: String? = null // ": "string",
    var CertNo: String? = null // ": "string",
    var TelNo: String? = null // ": "string",
    var UserName: String? = null // ": "string",
    var AssistCheckNo: String? = null // ": "string",
    var TravelDate: String? = null // ": "2019-09-19T05:37:25.944Z", -> 游玩日期
    var RebatePrice: String? = null // ": 0,
    var TicketmodelCode: String? = null // ": "string",
    var TicketmodelName: String? = null // ": "string", -> 票型名称
    var TicketmodelEngName: String? = null // ": "string",
    var TicketmodelPyCode: String? = null // ": "string",
    var TicketmodelType: String? = null // ": "string",
    var TicketmodelGroupCode: String? = null // ": "string",
    var TicketmodelKind: String? = null // ": "string",
    var SeasonType: String? = null // ": "string",
    var Validdays: String? = null // ": 0,
    var UserCount: String? = null // ": 0,
    var Remark: String? = null // ": "string",
    var TicketmodelKindName: String? = null // ": "string" -> 票种

    // 自定义字段,表示累加的数量
    var number = 0

}