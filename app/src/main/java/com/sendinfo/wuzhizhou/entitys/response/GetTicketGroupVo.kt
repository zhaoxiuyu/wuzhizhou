package com.sendinfo.wuzhizhou.entitys.response

class GetTicketGroupVo {

    var GroupCode: String? = null //": "734001",   -> 分组编码
    var GroupName: String? = null //": "门船票"    -> 分组名称

    // 用来分组的时候把对应的票型列表添加到分组里，显示的时候直接获取，不用多次过滤
    var tickets: MutableList<GetTicketVo>? = null

    // 分组是否被选中
    var isSelection = false

}