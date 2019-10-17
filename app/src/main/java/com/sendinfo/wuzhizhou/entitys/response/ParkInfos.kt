package com.sendinfo.wuzhizhou.entitys.response

class ParkInfos {

    var ParkName: String? = null // ": "string", -> 检票点
    var CheckFlag: String? = null // ": "string" -> 检票状态

    override fun toString(): String {
        return "检票点=$ParkName  ||  检票状态=$CheckFlag"
    }

}