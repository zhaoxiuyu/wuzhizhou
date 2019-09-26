package com.sendinfo.wuzhizhou.entitys.hardware

import com.sdt.Sdtapi
import java.nio.charset.Charset

/* 民族列表 */
var nation = arrayOf(
    "汉",
    "蒙古",
    "回",
    "藏",
    "维吾尔",
    "苗",
    "彝",
    "壮",
    "布依",
    "朝鲜",
    "满",
    "侗",
    "瑶",
    "白",
    "土家",
    "哈尼",
    "哈萨克",
    "傣",
    "黎",
    "傈僳",
    "佤",
    "畲",
    "高山",
    "拉祜",
    "水",
    "东乡",
    "纳西",
    "景颇",
    "克尔克孜",
    "土",
    "达斡尔",
    "仫佬",
    "羌",
    "布朗",
    "撒拉",
    "毛南",
    "仡佬",
    "锡伯",
    "阿昌",
    "普米",
    "塔吉克",
    "怒",
    "乌兹别克",
    "俄罗斯",
    "鄂温克",
    "德昂",
    "保安",
    "裕固",
    "京",
    "塔塔尔",
    "独龙",
    "鄂伦春",
    "赫哲",
    "门巴",
    "珞巴",
    "基诺"
)

// 读取身份证中的文字信息（可阅读格式的）
fun ReadBaseMsgToStr(msg: IdCardMsg, sdta: Sdtapi): Int {
    val ret: Int
    val puiCHMsgLen = IntArray(1)
    val puiPHMsgLen = IntArray(1)
    val pucCHMsg = ByteArray(256)
    val pucPHMsg = ByteArray(1024)
    // sdtapi中标准接口，输出字节格式的信息。
    ret = sdta.SDT_ReadBaseMsg(pucCHMsg, puiCHMsgLen, pucPHMsg, puiPHMsgLen)
    if (ret == 0x90) {
        try {
            val pucCHMsgStr = CharArray(128)
            DecodeByte(pucCHMsg, pucCHMsgStr)// 将读取的身份证中的信息字节，解码成可阅读的文字
            PareseItem(pucCHMsgStr, msg) // 将信息解析到msg中
            msg.ptoto = pucPHMsg
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    return ret
}

// 字节解码函数
@Throws(Exception::class)
fun DecodeByte(msg: ByteArray, msg_str: CharArray) {
    val newmsg = ByteArray(msg.size + 2)
    newmsg[0] = 0xff.toByte()
    newmsg[1] = 0xfe.toByte()
    for (i in msg.indices)
        newmsg[i + 2] = msg[i]
    val s = String(newmsg, Charset.forName("UTF-16"))
    for (i in 0 until s.toCharArray().size)
        msg_str[i] = s.toCharArray()[i]
}

// 分段信息提取
fun PareseItem(pucCHMsgStr: CharArray, msg: IdCardMsg) {
    msg.name = String(pucCHMsgStr, 0, 15)
    val sex_code = String(pucCHMsgStr, 15, 1)
    if (sex_code == "1")
        msg.sex = "男"
    else if (sex_code == "2")
        msg.sex = "女"
    else if (sex_code == "0")
        msg.sex = "未知"
    else if (sex_code == "9")
        msg.sex = "未说明"
    val nation_code = String(pucCHMsgStr, 16, 2)
    msg.nation_str = nation[Integer.valueOf(nation_code) - 1]
    msg.birth_year = String(pucCHMsgStr, 18, 4)
    msg.birth_month = String(pucCHMsgStr, 22, 2)
    msg.birth_day = String(pucCHMsgStr, 24, 2)
    msg.address = String(pucCHMsgStr, 26, 35)
    msg.id_num = String(pucCHMsgStr, 61, 18)
    msg.sign_office = String(pucCHMsgStr, 79, 15)
    msg.useful_s_date_year = String(pucCHMsgStr, 94, 4)
    msg.useful_s_date_month = String(pucCHMsgStr, 98, 2)
    msg.useful_s_date_day = String(pucCHMsgStr, 100, 2)
    msg.useful_e_date_year = String(pucCHMsgStr, 102, 4)
    msg.useful_e_date_month = String(pucCHMsgStr, 106, 2)
    msg.useful_e_date_day = String(pucCHMsgStr, 108, 2)
}
