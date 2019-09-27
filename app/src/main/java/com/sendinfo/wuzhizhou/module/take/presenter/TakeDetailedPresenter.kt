package com.sendinfo.wuzhizhou.module.take.presenter

import com.base.library.entitys.BaseResponse
import com.base.library.http.BRequest
import com.base.library.mvp.BPresenterImpl
import com.base.library.util.ArithMultiply
import com.base.library.util.JsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.reflect.TypeToken
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.entitys.request.SaveOrderReq
import com.sendinfo.wuzhizhou.entitys.request.TicketInfosReq
import com.sendinfo.wuzhizhou.entitys.response.PrintTempVo
import com.sendinfo.wuzhizhou.entitys.response.TakeTicketModelsVo
import com.sendinfo.wuzhizhou.module.take.contract.TakeDetailedContract
import com.sendinfo.wuzhizhou.utils.SaveOrder
import com.sendinfo.wuzhizhou.utils.getPrintNumber
import com.sendinfo.wuzhizhou.utils.getShebeiCode

class TakeDetailedPresenter(view: TakeDetailedContract.View) :
    BPresenterImpl<TakeDetailedContract.View>(view), TakeDetailedContract.Presenter {

    override fun saveOrder(takeTicketModels: MutableList<TakeTicketModelsVo>?, uuid: String) {
        var sum = 0.0
        var count = 0
        var billNo = ""
        var ticketInfoVos = mutableListOf<TicketInfosReq>()
        takeTicketModels?.forEach {
            if (it.number > 0) {
                // 每个票型
                val ticketInfoVo = TicketInfosReq()
                ticketInfoVo.BillDetailNo = it.BillDetailNo
                ticketInfoVo.TicketModelCode = it.TicketmodelCode
                ticketInfoVo.TicketModelName = it.TicketmodelName
                ticketInfoVo.TicketModelPrice = it.TicketPrice
                ticketInfoVo.TicketCount = it.number
                ticketInfoVos.add(ticketInfoVo)

                // 累加取票数量
                count += it.number

                // 数量 * 价格
                sum += ArithMultiply(it.number.toDouble(), it.TicketPrice)

                // 赋值 取票的 订单号
                billNo = it.BillNo ?: ""
            }
        }

        if (count <= 0) {
            ToastUtils.showShort("请增加取票数量")
            return
        }

        //打印纸票数不足,请管理员重新设置
        if (getPrintNumber() < count) {
            ToastUtils.showShort("打印纸票数不足,请管理员重新设置")
            return
        }

        val saveOrderVo = SaveOrderReq()
        saveOrderVo.OptType = "1"
        saveOrderVo.TerminalCode = getShebeiCode()
        saveOrderVo.PayTypeCode = "07"
        saveOrderVo.PayTypeName = "电子商务"
        saveOrderVo.PaySum = sum
        saveOrderVo.TotalTicketCount = count
        saveOrderVo.BillNo = billNo
        saveOrderVo.LockGuid = uuid
        saveOrderVo.TicketInfos = ticketInfoVos

        val bRequest = BRequest(SaveOrder).apply {
            body = JsonUtils.toJson(saveOrderVo)
        }
        getData(bRequest)
    }

    override fun requestSuccess(baseResponse: BaseResponse, bHttpDto: BRequest) {
        super.requestSuccess(baseResponse, bHttpDto)
        if (baseResponse.success) {
            baseResponse.data?.let {
                val printTemp =
                    JsonUtils.toAny(it, object : TypeToken<MutableList<PrintTempVo>>() {}) as MutableList<PrintTempVo>
                if (printTemp.isNullOrEmpty()) {
                    showDialogFinish("没有获取到打印模板", bHttpDto.isFinish)
                } else {
                    mView?.toPrintTemp(printTemp)
                }
            }
        } else {
            showDialogFinish(baseResponse.message, bHttpDto.isFinish)
        }
    }

    private fun showDialogFinish(message: String, finish: Boolean) {
        val listener = if (finish) mView?.getConfirmFinishListener() else mView?.getConfirmDisListener()
        mView?.showDialog(content = message, confirmListener = listener)
    }

}