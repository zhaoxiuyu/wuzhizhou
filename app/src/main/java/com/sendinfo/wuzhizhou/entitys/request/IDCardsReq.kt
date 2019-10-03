package com.sendinfo.wuzhizhou.entitys.request

import java.io.Serializable

class IDCardsReq : Serializable {

    var FName: String? = null // ": "string", -> 姓名
    var FNumber: String? = null // ": "string", -> 身份证号码
    var FSex: String? = null // ": "string", -> 性别
    var FAddress: String? = null // ": "string" -> 地址

}