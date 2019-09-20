package com.sendinfo.wuzhizhou.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.sendinfo.wuzhizhou.entitys.response.TakeOrderModelsVo
import com.sendinfo.wuzhizhou.entitys.response.TakeTicketModelsVo
import com.sendinfo.wuzhizhou.module.common.ui.PrintActivity
import com.sendinfo.wuzhizhou.module.take.ui.TakeDetailedActivity
import com.sendinfo.wuzhizhou.module.take.ui.TakeOrderActivity
import java.io.Serializable

fun <T> startAct(context: Context, cls: Class<T>, isFinish: Boolean = true) {
    val intent = Intent(context, cls)
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
fun startActPrint(context: Context, printTemp: MutableList<String>, isFinish: Boolean = true) {
    val intent = Intent(context, PrintActivity::class.java)
    intent.putExtra("printTemp", printTemp as Serializable)
    context.startActivity(intent)
    if (isFinish && context is Activity) context.finish()
}

