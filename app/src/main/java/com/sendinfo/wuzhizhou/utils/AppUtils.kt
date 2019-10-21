package com.sendinfo.wuzhizhou.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.sendinfo.wuzhizhou.entitys.response.PrintTempVo
import com.sendinfo.wuzhizhou.entitys.response.TakeOrderModelsVo
import com.sendinfo.wuzhizhou.entitys.response.TakeTicketModelsVo
import com.sendinfo.wuzhizhou.module.common.ui.PrintActivityNew
import com.sendinfo.wuzhizhou.module.take.ui.TakeDetailedActivity
import com.sendinfo.wuzhizhou.module.take.ui.TakeOrderActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Serializable

fun <T> startAct(context: Context, cls: Class<T>, isFinish: Boolean = true) {
    val intent = Intent(context, cls)
    context.startActivity(intent)
    if (isFinish && context is Activity) context.finish()
}

fun startAct(context: Context, intent: Intent, isFinish: Boolean = true) {
    context.startActivity(intent)
    if (isFinish && context is Activity) context.finish()
}

// 去取票订单页面
fun startActTakeOrder(context: Context, takeOrderModels: MutableList<TakeOrderModelsVo>) {
    val intent = Intent(context, TakeOrderActivity::class.java)
    intent.putExtra("takeOrderModels", takeOrderModels as Serializable)
    context.startActivity(intent)
    if (context is Activity) context.finish()
}

// 去取票明细页面
fun startActTakeDetailed(
    context: Context,
    uuid: String,
    takeTicketModels: MutableList<TakeTicketModelsVo>
) {
    val intent = Intent(context, TakeDetailedActivity::class.java)
    intent.putExtra("uuid", uuid)
    intent.putExtra("takeTicketModels", takeTicketModels as Serializable)
    context.startActivity(intent)
    if (context is Activity) context.finish()
}

// 去打印
fun startActPrint(context: Context, printTemp: MutableList<PrintTempVo>, source: String) {
//    val intent = Intent(context, PrintActivity::class.java)
    val intent = Intent(context, PrintActivityNew::class.java)
    intent.putExtra("source", source)
    intent.putExtra("printTemp", printTemp as Serializable)
    context.startActivity(intent)
    if (context is Activity) context.finish()
}

// 默认打印模板
fun defaultTemplate(): String {
    return "CUT ON\n" +
            "\n" +
            "DIR3\n" +
            "\n" +
            "NASC \"UTF-8\"\n" +
            "\n" +
            "PP520,160\n" +
            "BARSET \"QRCODE\",1,1,6,2,1:PB \"你们真烦\"\n" +
            "\n" +
            "FT \"Microsoft YaHei\",8,0,145\n" +
            "\n" +
            "PP530,30\n" +
            "PT \"票名:\"\n" +
            "\n" +
            "FT \"Microsoft YaHei\",7,0,145\n" +
            "\n" +
            "PP380,60\n" +
            "PT \"交易号:\"\n" +
            "\n" +
            "PP380,90\n" +
            "\n" +
            "PT \"票价:\"\n" +
            "\n" +
            "PP380,120\n" +
            "PT \"合计:\"\n" +
            "\n" +
            "PP230,120\n" +
            "PT \"补打标识:\"\n" +
            "\n" +
            "PP380,150\n" +
            "\n" +
            "PT \"售票员:\"\n" +
            "\n" +
            "PP380,170\n" +
            "\n" +
            "PT \"支付方式:\"\n" +
            "\n" +
            "PP230,90\n" +
            "PT \"人数:\"\n" +
            "PP520,200\n" +
            "PT \"条码号:\"\n" +
            "\n" +
            "PP520,230\n" +
            "\n" +
            "PT \"游玩日期:\"\n" +
            "PP520,260\n" +
            "\n" +
            "PT \"出票时间:\"\n" +
            "\n" +
            "FT \"Microsoft YaHei\",8,0,145\n" +
            "\n" +
            "PP530,330\n" +
            "PT \"票名:测试票\"\n" +
            "FT \"Microsoft YaHei\",7,0,145\n" +
            "\n" +
            "PP520,360\n" +
            "PT \"交易号:\"\n" +
            "\n" +
            "PP520,390\n" +
            "PT \"票种:\"\n" +
            "\n" +
            "PP230,390\n" +
            "PT \"补打标识:\"\n" +
            "\n" +
            "PP520,420\n" +
            "PT \"票价:\"\n" +
            "\n" +
            "PP520,450\n" +
            "PT \"合计:\"\n" +
            "\n" +
            "PP520,480\n" +
            "PT \"支付方式:\"\n" +
            "\n" +
            "PP520,510\n" +
            "PT \"条码号:\"\n" +
            "PP520,540\n" +
            "\n" +
            "PT \"游客姓名:\"\n" +
            "PP520,570\n" +
            "PT \"售票员:\"\n" +
            "\n" +
            "PP520,600\n" +
            "PT \"售票点:\"\n" +
            "\n" +
            "PP520,630\n" +
            "PT \"出票时间:\"\n" +
            "PP520,660\n" +
            "PT \"游玩日期:\"\n" +
            "PP520,690\n" +
            "PT\"商户号:\"\n" +
            "PF1\n"

//    return "CUT  ON \n" +   //切纸
////    return "CUT  OFF \n" +    //不切纸
//            "DIR3\n" +
//            "NASC \"UTF-8\"\n" +
//            "FT \"Microsoft YaHei\",6,0,145\n" +
////            "FT \"FangSong\",6,0,145\n" +
//
//            "PP600,190 \n" +
//            "BARSET \"QRCODE\",1,1,6,2,1:" +
//            "PB \"barCode\" \n" +
//
//            "PP450,80\n" +
//            "PT \"人数: 1 \"\n" +
//
//            "PP450,110\n" +
//            "PT \"名称: 张三李四王二麻子 \"\n" +
//
//            "PP450,170\n" +
//            "PT \"生效日期 : 2019-09-24 \"\n" +
//
//            "PP450,200\n" +
//            "PT \"票价: 1.0 \"\n" +
//            "PF1\n"
}

fun getFromAssets(context: Context, fileName: String): String {
    val sb = StringBuilder()
    try {
        val inputReader = InputStreamReader(context.resources.assets.open(fileName))
        val bufReader = BufferedReader(inputReader)
        var line: String?
        while (true) {
            line = bufReader.readLine()
            if (line == null) break
            else sb.append(line)
        }
        return sb.toString()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return sb.toString()
}
