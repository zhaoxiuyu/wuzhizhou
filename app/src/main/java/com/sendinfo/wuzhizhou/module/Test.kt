package com.sendinfo.wuzhizhou.module

/**
 * 语音文件说明
 *      (fuzhuma = 请输入辅助码)
 *      (weixinpay = 请打开微信进行扫码支付)
 *      (zhifubaopay = 请打开支付宝进行扫码支付)
 *      (wxorzybsaomazhifu = 打开微信或支付宝进行扫码支付)
 *      (qrcode = 请将二维码放置于扫描区域)
 *      (idcard = 请将身份证放置于感应区)
 *      (order = 请选择订单)
 *      (take = 请选择你要取的票型)
 *      (purchase = 请选择要购买的票型)
 *      (idcarname = 请进行实名认证)
 *      (paytype = 请选择支付方式)
 *      (chupiao = 正在出票，请留意自助机下方出票口，下方出票口取票，一人一票，一人一票)
 *      (taketype = 请选择取票方式)
 *      (qpslcgxz = 您的取票数量超过限制数量，请到柜台取票)
 *      (yueduxuzhi = 请仔细阅读须知)
 *      (piaozhibuzu = 可打印票纸数量不足，请联系景区管理人员)
 *      (ysf = 请打开银联云闪付进行扫码支付)
 */


val string = "CUT ON\n" +
        "\n" +
        "DIR3\n" +
        "\n" +
        "NASC \"UTF-8\"\n" +
        "\n" +
        "PP520,160\n" +
        "BARSET \"QRCODE\",1,1,6,2,1:PB \"${123}\"\n" +
        "\n" +
        "FT \"Microsoft YaHei\",8,0,145\n" +
        "\n" +
        "PP530,30\n" +
        "PT \"票名:${123}:0:5:\"\n" +
        "\n" +
        "FT \"Microsoft YaHei\",7,0,145\n" +
        "\n" +
        "PP380,60\n" +
        "PT \"交易号:${123}\"\n" +
        "\n" +
        "PP380,90\n" +
        "\n" +
        "PT \"票价:${123}\"\n" +
        "\n" +
        "PP380,120\n" +
        "PT \"合计:${123}\"\n" +
        "\n" +
        "PP230,120\n" +
        "PT \"补打标识:${123}\"\n" +
        "\n" +
        "PP380,150\n" +
        "\n" +
        "PT \"售票员:${123}\"\n" +
        "\n" +
        "PP380,170\n" +
        "\n" +
        "PT \"支付方式:${123}\"\n" +
        "\n" +
        "PP230,90\n" +
        "PT \"人数:${123}\"\n" +
        "PP520,200\n" +
        "PT \"条码号:${123}\"\n" +
        "\n" +
        "PP520,230\n" +
        "\n" +
        "PT \"游玩日期:${123}\"\n" +
        "PP520,260\n" +
        "\n" +
        "PT \"出票时间:${123}\"\n" +
        "FT \"Microsoft YaHei\",6,0,145\n" +
        "\n" +
        "PP530,300\n" +
        "\n" +
        "PT \":${123}:0:5:\"\n" +
        "FT \"Microsoft YaHei\",9,0,145\n" +
        "\n" +
        "PP520,330\n" +
        "\n" +
        "PT \"票名:${123}\"\n" +
        "\n" +
        "FT \"Microsoft YaHei\",7,0,145\n" +
        "\n" +
        "PP520,360\n" +
        "PT \"交易号:${123}\"\n" +
        "\n" +
        "PP520,390\n" +
        "PT \"票种:${123}\"\n" +
        "\n" +
        "PP230,390\n" +
        "PT \"补打标识:${123}\"\n" +
        "\n" +
        "PP520,420\n" +
        "PT \"票价:${123}\"\n" +
        "\n" +
        "PP520,450\n" +
        "PT \"合计:${123}\"\n" +
        "\n" +
        "PP520,480\n" +
        "PT \"支付方式:${123}\"\n" +
        "\n" +
        "PP520,510\n" +
        "PT \"条码号:${123}\"\n" +
        "PP520,540\n" +
        "\n" +
        "PT \"游客姓名:${123}\"\n" +
        "PP520,570\n" +
        "PT \"售票员:${123}\"\n" +
        "\n" +
        "PP520,600\n" +
        "PT \"售票点:${123}\"\n" +
        "\n" +
        "PP520,630\n" +
        "PT \"出票时间:${123}\"\n" +
        "PP520,660\n" +
        "PT \"游玩日期:${123}\"\n" +
        "PP520,690\n" +
        "PT\"商户号:\"\n" +
        "PP520,710\n" +
        "PT\"${123}\"\n" +
        "PP520,740\n" +
        "PT\":${123123123123}:0:5:\"\n" +
        "PP520,770\n" +
        "PT\":${123123123123123}:5:10:\"\n" +
        "PF1\n" +
        "\n" +
        "\n"

fun main(args: Array<String>) {
//    System.out.println(UUID.randomUUID().toString().replace("-", ""))
//    System.out.println(
//        TimeUtils.date2String(
//            Date(),
//            "yyyyMMddmmHHssSSS"
//        ) + ((Math.random() * 9 + 1) * 1000000).toInt()
//    )

//    rep(string)


//    val string = "429001199311153156"
//    val sb = java.lang.StringBuilder(string)
//    System.out.println("${sb.replace(6, 12, "******")}")
//    System.out.println("${sb.replace(6, 12, "******")}")

}

fun rep(template: String): String {
    val sBuilder = StringBuilder()

    val datas = template.split("\n")
    System.out.println("${datas.size}")
    // 拆分模板为每一行进行循环
    for (i in datas.indices) {
        val data = datas[i]
        // 拆分每一行的模板,用:进行分割,如果长度为5就说明符合规则,进行字符串截取
        val str = data.split(":")
        if (str.size == 5) {
            // 截取的开始位和结束位
            var start = 0
            var end = 0
            try {
                start = Integer.valueOf(str[2])
                end = Integer.valueOf(str[3])
            } catch (e: Exception) {
            }

            var rep = str[1]
            //开始截取位小于等 内容的长度
            if (start <= rep.length) {
                if (end <= rep.length) {//结束位小于等 内容的长度,说明可以截取
                    rep = rep.substring(start, end)
                } else {//结束位大于等 内容的长度,说明不在截取范围内,就显示剩余部分
                    rep = rep.substring(start, rep.length)
                }
            } else {
                rep = ""
            }

            val newStr = if (str[0].trim().length <= 5) {
                str[0] + rep + str[4]
            } else {
                str[0] + ":" + rep + str[4]
            }
            sBuilder.appendln(newStr)

        } else {
            sBuilder.appendln(data)
        }
    }
    System.out.println("最终模板如下 : ")
    System.out.println(sBuilder.toString())
    return sBuilder.toString()
}

