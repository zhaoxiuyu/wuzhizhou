package com.sendinfo.wuzhizhou.entitys.response

import java.io.Serializable

class Notice : Serializable {
    var SaleNote: String = ""
    var TakeNote: String = ""
    var SplashImages: List<String>? = null
}