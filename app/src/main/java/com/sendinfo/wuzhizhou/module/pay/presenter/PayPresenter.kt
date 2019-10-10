package com.sendinfo.wuzhizhou.module.pay.presenter

import androidx.lifecycle.LifecycleOwner
import com.base.library.entitys.BaseResponse
import com.base.library.http.BRequest
import com.base.library.mvp.BPresenterImpl
import com.base.library.util.JsonUtils
import com.base.library.util.MD5Utils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.TimeUtils
import com.google.gson.reflect.TypeToken
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.sendinfo.wuzhizhou.entitys.request.PayReq
import com.sendinfo.wuzhizhou.entitys.request.SaveOrderReq
import com.sendinfo.wuzhizhou.entitys.response.PayRsp
import com.sendinfo.wuzhizhou.entitys.response.PrintTempVo
import com.sendinfo.wuzhizhou.module.pay.contract.PayContract
import com.sendinfo.wuzhizhou.utils.CloseQRCode
import com.sendinfo.wuzhizhou.utils.GetQRCode
import com.sendinfo.wuzhizhou.utils.Query
import com.sendinfo.wuzhizhou.utils.SaveOrder
import java.math.BigDecimal
import java.text.SimpleDateFormat

class PayPresenter(view: PayContract.View) : BPresenterImpl<PayContract.View>(view), PayContract.Presenter {

    private var lifecycleOwner: LifecycleOwner? = null

    override fun onCreate(owner: LifecycleOwner) {
        lifecycleOwner = owner
    }

    override fun saveOrder(saveOrderVo: SaveOrderReq) {
        val bRequest = BRequest(SaveOrder).apply {
            bodyJson = JsonUtils.toJson(saveOrderVo)
        }
        getData(bRequest)
    }

    override fun httpData(
        billno: String,
        mid: String,
        instMid: String,
        msgSrc: String,
        tid: String,
        paySum: Double,
        qrcodeID: String,
        secretKey: String,
        flag: Int
    ) {
        var requestTimestamp = TimeUtils.getNowString(SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
        var billdate = TimeUtils.getNowString(SimpleDateFormat("yyyy-MM-dd"))
        val builder = StringBuilder()
        val costPrice = BigDecimal(paySum)
        val doubleCostPrice = costPrice.setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
        var totalAmount = (doubleCostPrice * 100).toInt()
        //正式参数
        builder.append("billDate=$billdate&")
        builder.append("billNo=$billno&")
        builder.append("instMid=$instMid&")
        builder.append("mid=$mid&")
        builder.append("msgSrc=$msgSrc&")
        when (flag) {
            1 -> {
                builder.append("msgType=" + "bills.getQRCode" + "&")
                builder.append("requestTimestamp=$requestTimestamp&")
                builder.append("tid=$tid&")
                builder.append("totalAmount=$totalAmount")
            }
            2 -> {
                builder.append("msgType=" + "bills.query" + "&")
                builder.append("requestTimestamp=$requestTimestamp&")
                builder.append("tid=$tid&")
                builder.append("totalAmount=$totalAmount")
            }
            3 -> {
                builder.append("msgType=" + "bills.closeQRCode" + "&")
                builder.append("qrCodeId=$qrcodeID&")
                builder.append("requestTimestamp=$requestTimestamp&")
                builder.append("tid=$tid")
            }
        }
        val payReq = PayReq()
        payReq.requestTimestamp = requestTimestamp
        payReq.billDate = billdate
        payReq.billNo = billno
        payReq.instMid = instMid
        payReq.mid = mid
        payReq.tid = tid
        LogUtils.i(builder.toString() + secretKey)
        payReq.sign = MD5Utils.string2MD5(builder.toString() + secretKey).toUpperCase()
        LogUtils.i(payReq.sign)
        payReq.msgSrc = msgSrc
        var urlFull = ""
        val bRequest = BRequest("$flag")
        when (flag) {
            1 -> {
                payReq.msgType = "bills.getQRCode"
                payReq.totalAmount = totalAmount.toString()
                urlFull = GetQRCode
                bRequest.isFinish = true
            }
            2 -> {
                payReq.msgType = "bills.query"
                payReq.totalAmount = totalAmount.toString()
                urlFull = Query
                bRequest.silence = true
            }
            3 -> {
                payReq.msgType = "bills.closeQRCode"
                payReq.qrCodeId = qrcodeID
                urlFull = CloseQRCode
            }
        }
        bRequest.bodyJson = JsonUtils.toJson(payReq)
        bRequest.fullUrl = urlFull
        LogUtils.i(JsonUtils.toJson(bRequest))
        getData(bRequest)
    }

    override fun requestSuccess(body: String, bHttpDto: BRequest) {
        when (bHttpDto.method) {
            "1" -> {
                mView?.disDialog()
                var payRsp = JsonUtils.toAny(body, PayRsp::class.java)
                if (payRsp.errCode == "SUCCESS") {
                    mView?.onGetQRCode(payRsp)
                } else {
                    showDialogFinish(payRsp.errMsg ?: "获取支付二维码失败", true)
                }
            }
            "2" -> {
                var payRsp = JsonUtils.toAny(body, PayRsp::class.java)
                mView?.onQuery(payRsp)
            }
            "3" -> {
                mView?.disDialog()
                mView?.onCloseQRCode()
            }
            SaveOrder -> { //保存订单
                mView?.disDialog()
                var baseResponse = JsonUtils.toAny(body, BaseResponse::class.java)
                if (baseResponse.success) {
                    baseResponse.data?.let {
                        val printTemp = JsonUtils.toAny(it,
                            object : TypeToken<MutableList<PrintTempVo>>() {}) as MutableList<PrintTempVo>
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
        }
//        when (bHttpDto.url) {
//            GetQRCode -> {
//                mView?.disDialog()
//                var payRsp = JsonUtils.toAny(body, PayRsp::class.java)
//                if (payRsp.errCode == "SUCCESS") {
//                    mView?.onGetQRCode(payRsp)
//                } else {
//                    showDialogFinish(payRsp.errMsg ?: "获取支付二维码失败", true)
//                }
//            }
//            Query -> {
//                var payRsp = JsonUtils.toAny(body, PayRsp::class.java)
//                mView?.onQuery(payRsp)
//            }
//            CloseQRCode -> {
//                mView?.disDialog()
//                mView?.onCloseQRCode()
//            }
//            else -> { //保存订单
//                mView?.disDialog()
//                var baseResponse = JsonUtils.toAny(body, BaseResponse::class.java)
//                if (baseResponse.success) {
//                    baseResponse.data?.let {
//                        val printTemp = JsonUtils.toAny(it,
//                            object : TypeToken<MutableList<PrintTempVo>>() {}) as MutableList<PrintTempVo>
//                        if (printTemp.isNullOrEmpty()) {
//                            showDialogFinish("没有获取到打印模板", bHttpDto.isFinish)
//                        } else {
//                            mView?.toPrintTemp(printTemp)
//                        }
//                    }
//                } else {
//                    showDialogFinish(baseResponse.message, bHttpDto.isFinish)
//                }
//            }
//        }
    }

    override fun requestError(throwable: Throwable?, baseHttpDto: BRequest) {
        super.requestError(throwable, baseHttpDto)
        when (baseHttpDto.url) {
            Query -> mView?.trainQueryOrder()//查询订单支付结果
            CloseQRCode -> mView?.onCloseQRCode()//关闭页面
            SaveOrder -> {
                mView?.showDialog("网络错误", "下单网络错误，是否重试", confirmListener = OnConfirmListener {
                    getData(baseHttpDto)
                }, cancelListener = mView?.getCancelFinishListener())
            }
        }
    }

    // 统一的,只要返回错误或者没有数据都给出提示并且退出页面
    private fun showDialogFinish(message: String, finish: Boolean) {
        val listener = if (finish) mView?.getConfirmFinishListener() else mView?.getConfirmDisListener()
        mView?.showDialog(content = message, confirmListener = listener)
    }

}