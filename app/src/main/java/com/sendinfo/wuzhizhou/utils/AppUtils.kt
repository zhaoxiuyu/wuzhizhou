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
fun startActTakeDetailed(context: Context, uuid: String, takeTicketModels: MutableList<TakeTicketModelsVo>) {
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
    return "CUT  ON \n" +   //切纸
//    return "CUT  OFF \n" +    //不切纸
            "DIR3\n" +
            "NASC \"UTF-8\"\n" +
            "FT \"Microsoft YaHei\",6,0,145\n" +
//            "FT \"FangSong\",6,0,145\n" +

            "PP600,190 \n" +
            "BARSET \"QRCODE\",1,1,6,2,1:" +
            "PB \"barCode\" \n" +

            "PP450,80\n" +
            "PT \"人数: 1 \"\n" +

            "PP450,110\n" +
            "PT \"名称: 张三李四王二麻子 \"\n" +

            "PP450,170\n" +
            "PT \"生效日期 : 2019-09-24 \"\n" +

            "PP450,200\n" +
            "PT \"票价: 1.0 \"\n" +
            "PF1\n"
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
