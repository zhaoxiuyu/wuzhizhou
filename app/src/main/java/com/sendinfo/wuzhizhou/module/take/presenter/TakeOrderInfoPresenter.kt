package com.sendinfo.wuzhizhou.module.take.presenter

import com.base.library.entitys.BaseResponse
import com.base.library.http.BRequest
import com.base.library.mvp.BPresenterImpl
import com.base.library.util.JsonUtils
import com.sendinfo.wuzhizhou.entitys.response.GetTakeOrderInfoVo
import com.sendinfo.wuzhizhou.module.take.contract.TakeOrderInfoContract
import com.sendinfo.wuzhizhou.utils.GetTakeOrderInfo

/**
 * 用于身份证 辅助吗 二维码 查询取票订单 明细
 */
class TakeOrderInfoPresenter(view: TakeOrderInfoContract.View) :
    BPresenterImpl<TakeOrderInfoContract.View>(view), TakeOrderInfoContract.Presenter {

    // 请求订单 或者 明细
    override fun getTakeOrderInfo(takeFlag: String, keyword: String, lockGuid: String, finish: Boolean) {
        val map = mapOf(
            "takeFlag" to takeFlag
            , "keyword" to keyword
            , "lockGuid" to lockGuid
        )
        val bRequest = BRequest(GetTakeOrderInfo).apply {
            httpType = BRequest.GET
            params = map
            isFinish = finish
        }
        getData(bRequest)
    }

    override fun requestSuccess(baseResponse: BaseResponse, bHttpDto: BRequest) {
        super.requestSuccess(baseResponse, bHttpDto)
        if (baseResponse.success) {
            when (bHttpDto.method) {
                GetTakeOrderInfo -> baseResponse.data?.let { analysisTakeOrderInfo(it, bHttpDto.isFinish) }
            }
        } else {
            showDialogFinish(baseResponse.message, bHttpDto.isFinish)
        }
    }

    // 解析 订单 或者 明细
    private fun analysisTakeOrderInfo(data: Any, finish: Boolean) {
        val takeOrderInfoVo = JsonUtils.toAny(data, GetTakeOrderInfoVo::class.java)
        if (takeOrderInfoVo.IsOrder.equals("1")) { // 1是订单，否则是明细
            val orders = takeOrderInfoVo.TakeOrderModels
            if (orders.isNullOrEmpty()) {
                showDialogFinish("没有订单", finish)
            } else {
                mView?.toOrder(orders)
            }
        } else {
            val detaileds = takeOrderInfoVo.TakeTicketModels
            if (detaileds.isNullOrEmpty()) {
                showDialogFinish("没有明细", finish)
            } else {
                mView?.toDetailed(detaileds)
            }
        }
    }

    private fun showDialogFinish(message: String, finish: Boolean) {
        val listener = if (finish) mView?.getConfirmFinishListener() else mView?.getConfirmDisListener()
        mView?.showDialog(content = message, confirmListener = listener)
    }

}