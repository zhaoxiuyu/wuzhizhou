package com.sendinfo.wuzhizhou.module.again.presenter

import com.base.library.entitys.BaseResponse
import com.base.library.http.BRequest
import com.base.library.mvp.BPresenterImpl
import com.base.library.util.JsonUtils
import com.google.gson.reflect.TypeToken
import com.sendinfo.wuzhizhou.entitys.response.PrintTempVo
import com.sendinfo.wuzhizhou.entitys.response.QueryTradeVo
import com.sendinfo.wuzhizhou.module.again.contract.AgainRecordContract
import com.sendinfo.wuzhizhou.utils.QueryTrade
import com.sendinfo.wuzhizhou.utils.ReprintTicket
import com.sendinfo.wuzhizhou.utils.getShebeiCode

class AgainRecordPresenter(view: AgainRecordContract.View) :
    BPresenterImpl<AgainRecordContract.View>(view), AgainRecordContract.Presenter {

    override fun queryTrade(tradeId: String, certNo: String, assistCheckNo: String) {
        val map = mapOf(
            "terminalCode" to getShebeiCode(),
            "tradeId" to tradeId,
            "certNo" to certNo,
            "assistCheckNo" to assistCheckNo
        )
        val bRequest = BRequest(QueryTrade).apply {
            httpType = BRequest.GET
            params = map
        }
        getData(bRequest)
    }

    override fun reprintTicket(oldBarcode: String) {
        val map = mapOf("terminalCode" to getShebeiCode(), "oldBarcode" to oldBarcode)
        val bRequest = BRequest(ReprintTicket).apply {
            params = map
        }
        getData(bRequest)
    }

    override fun requestSuccess(baseResponse: BaseResponse, bHttpDto: BRequest) {
        super.requestSuccess(baseResponse, bHttpDto)
        if (baseResponse.success) {
            when (bHttpDto.method) {
                QueryTrade -> baseResponse.data?.let { analysisQueryTrade(it, bHttpDto.isFinish) }
                ReprintTicket -> baseResponse.data?.let { analysisReprintTicket(it, bHttpDto.isFinish) }
            }
        } else {
            showDialogFinish(baseResponse.message, bHttpDto.isFinish)
        }
    }

    // 解析重打查询列表
    private fun analysisQueryTrade(data: Any, finish: Boolean) {
        val queryTradeVo =
            JsonUtils.toAny(data, object : TypeToken<MutableList<QueryTradeVo>>() {}) as MutableList<QueryTradeVo>
        if (queryTradeVo.isNullOrEmpty()) {
            showDialogFinish("没有查询到数据", finish)
        } else {
            mView?.queryTradeSuccess(queryTradeVo)
        }
    }

    // 解析重打获取打印模板
    private fun analysisReprintTicket(data: Any, finish: Boolean) {
        val printTemp =
            JsonUtils.toAny(data, object : TypeToken<MutableList<PrintTempVo>>() {}) as MutableList<PrintTempVo>
        if (printTemp.isNullOrEmpty()) {
            showDialogFinish("没有获取到打印模板", finish)
        } else {
            mView?.reprintTicketSuccess(printTemp)
        }
    }

    private fun showDialogFinish(message: String, finish: Boolean) {
        val listener = if (finish) mView?.getConfirmFinishListener() else mView?.getConfirmDisListener()
        mView?.showDialog(content = message, confirmListener = listener)
    }

}