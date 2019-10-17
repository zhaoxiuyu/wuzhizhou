package com.sendinfo.wuzhizhou.entitys.response

import java.io.Serializable

// 购票须知
class Notice : Serializable {
    var SaleNote: String = ""
    var TakeNote: String = ""

    // 图片
    var SplashImages: List<String>? = null
}