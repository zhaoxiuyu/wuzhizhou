package com.sendinfo.wuzhizhou.entitys.response

import java.io.Serializable

class TakeOrderModelsVo : Serializable {

    var BillNo: String? = null // ": "string", -> 订单号
    var TravelDate: String? = null // ": "string", -> 游玩日期
    var UserName: String? = null // ": "string", -> 预定人
    var Remark: String? = null // ": "string",
    var TelNo: String? = null // ": "string",
    var ClientName: String? = null // ": "string", -> 分销商名称
    var AssistCheckNo: String? = null // ": "string"

}