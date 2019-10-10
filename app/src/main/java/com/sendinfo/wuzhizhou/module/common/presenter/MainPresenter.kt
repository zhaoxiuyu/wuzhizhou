package com.sendinfo.wuzhizhou.module.common.presenter

import com.base.library.entitys.BaseResponse
import com.base.library.http.BRequest
import com.base.library.mvp.BPresenterImpl
import com.base.library.util.JsonUtils
import com.blankj.utilcode.util.LogUtils
import com.sendinfo.wuzhizhou.entitys.response.Notice
import com.sendinfo.wuzhizhou.module.purchase.contract.MainContract
import com.sendinfo.wuzhizhou.utils.QueryNotice
import com.sendinfo.wuzhizhou.utils.getShebeiCode

class MainPresenter(view: MainContract.View) : BPresenterImpl<MainContract.View>(view), MainContract.Presenter {

    override fun queryNotice() {
        val map = mapOf("terminalCode" to getShebeiCode())
        val bRequest = BRequest(QueryNotice).apply {
            httpType = BRequest.GET
            silence = true
            params = map
        }
        getData(bRequest)
    }

    override fun requestSuccess(baseResponse: BaseResponse, bHttpDto: BRequest) {
        super.requestSuccess(baseResponse, bHttpDto)
        if (baseResponse.success) {
            LogUtils.d(bHttpDto.method)
            baseResponse.data?.let {
                val tickets = JsonUtils.toAny(it, Notice::class.java)
                mView?.queryNoticeSuccess(tickets)
            }
        } else {
            showDialogFinish(baseResponse.message, bHttpDto.isFinish)
        }
    }

    // 统一的,只要返回错误或者没有数据都给出提示并且退出页面
    private fun showDialogFinish(message: String, finish: Boolean) {
        val listener = if (finish) mView?.getConfirmFinishListener() else mView?.getConfirmDisListener()
        mView?.showDialog(content = message, confirmListener = listener)
    }
}