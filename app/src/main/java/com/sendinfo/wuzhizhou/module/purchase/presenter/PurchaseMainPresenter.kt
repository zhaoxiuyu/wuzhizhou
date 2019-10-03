package com.sendinfo.wuzhizhou.module.purchase.presenter

import com.base.library.entitys.BaseResponse
import com.base.library.http.BRequest
import com.base.library.mvp.BPresenterImpl
import com.base.library.util.JsonUtils
import com.blankj.utilcode.util.LogUtils
import com.google.gson.reflect.TypeToken
import com.sendinfo.wuzhizhou.entitys.response.GetTicketGroupVo
import com.sendinfo.wuzhizhou.entitys.response.GetTicketVo
import com.sendinfo.wuzhizhou.module.purchase.contract.MainContract
import com.sendinfo.wuzhizhou.module.purchase.contract.PurchaseMainContract
import com.sendinfo.wuzhizhou.utils.GetTicket
import com.sendinfo.wuzhizhou.utils.GetTicketGroup
import com.sendinfo.wuzhizhou.utils.getShebeiCode

class PurchaseMainPresenter(view: PurchaseMainContract.View) :
    BPresenterImpl<PurchaseMainContract.View>(view), PurchaseMainContract.Presenter {

    // 请求分组
    override fun getTicketGroup() {
        val bRequest = BRequest(GetTicketGroup).apply {
            httpType = BRequest.GET
            isFinish = true
        }
        getData(bRequest)
    }

    // 请求票型
    override fun getTicket() {
        val map = mapOf("terminalCode" to getShebeiCode())
        val bRequest = BRequest(GetTicket).apply {
            httpType = BRequest.GET
            params = map
            isFinish = true
        }
        getData(bRequest)
    }

    // 不同的接口 进行不同的解析
    override fun requestSuccess(baseResponse: BaseResponse, bHttpDto: BRequest) {
        super.requestSuccess(baseResponse, bHttpDto)
        if (baseResponse.success) {
            LogUtils.d(bHttpDto.method)
            when (bHttpDto.method) {
                GetTicket -> baseResponse.data?.let { analysisTicket(it, bHttpDto.isFinish) }
                GetTicketGroup -> baseResponse.data?.let { analysisTicketGroup(it, bHttpDto.isFinish) }
            }
        } else {
            showDialogFinish(baseResponse.message, bHttpDto.isFinish)
        }
    }

    // 解析票型
    private fun analysisTicket(data: Any, finish: Boolean) {
        val tickets =
            JsonUtils.toAny(data, object : TypeToken<MutableList<GetTicketVo>>() {}) as MutableList<GetTicketVo>
        if (tickets.isNullOrEmpty()) {
            showDialogFinish("没有获取到票型", finish)
        } else {
            mView?.getTicketSuccess(tickets)
            getTicketGroup() // 票型获取完成 自动 获取分组
        }
    }

    // 解析分组
    private fun analysisTicketGroup(data: Any, finish: Boolean) {
        val ticketGroup = JsonUtils.toAny(data,
            object : TypeToken<MutableList<GetTicketGroupVo>>() {}) as MutableList<GetTicketGroupVo>
        if (ticketGroup.isNullOrEmpty()) {
            showDialogFinish("没有获取到分组", finish)
        } else {
            mView?.getTicketGroupSuccess(ticketGroup)
        }
    }

    // 统一的,只要返回错误或者没有数据都给出提示并且退出页面
    private fun showDialogFinish(message: String, finish: Boolean) {
        val listener = if (finish) mView?.getConfirmFinishListener() else mView?.getConfirmDisListener()
        mView?.showDialog(content = message, confirmListener = listener)
    }

}