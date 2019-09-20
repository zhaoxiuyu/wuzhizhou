package com.sendinfo.wuzhizhou.entitys.response

import java.io.Serializable

class GetTicketVo : Serializable {

    var TicketModelCode: String? = null // ": "1018005",  ->  票型编号
    var TicketModelName: String? = null // ": "yy测试票型-导游票", ->  票型名称
    var TicketModelPyCode: String? = null // ": "yyCSPX-DYP",   ->  票型拼音简拼
    var TicketModelType: String? = null // ": "03", ->  票类型
    var TicketModelKind: String? = null // ": "101006", ->  票种
    var TicketModelKindName: String? = null // ": "免费", ->  票种名称
    var SeasonType: String? = null // ": "2",   ->  淡旺季类型
    var BarcodeType: String? = null // ": "MP", ->  介质类型
    var RebatePrice: String? = null // ": 0,    ->  票型价格
    var BuyLimitCoun: String? = null // t": 1,  ->  限制一次购票最大数量
    var Validdays: String? = null // ": 1,  ->  有效天数
    var UserCount: String? = null // ": 1,  ->  使用人数
    var FTicketType: String? = null // ": "导游", ->  票类型
    var NeedReadIDCard: String? = null // ": "0",   ->  购票是否需要读身份证
    var Isyh: String? = null // ": "0", ->  是否有优惠
    var YhRemark: String? = null // ": null,    ->  优惠弹出提示信息
    var TicketGroup: String? = null // ": "734002", ->  票分组,用这个过滤分组
    var TicketModelGroupCode: String? = null // ": "1018"   ->  票型分组

    // 自定义定义的属性,用来保存已选的票型数量
    var tvNumber = 0

}