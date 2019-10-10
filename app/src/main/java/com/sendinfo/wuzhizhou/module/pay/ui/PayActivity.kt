package com.sendinfo.wuzhizhou.module.pay.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.text.TextUtils
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import com.base.library.interfaces.OnSurplusListener
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.entitys.request.SaveOrderReq
import com.sendinfo.wuzhizhou.entitys.response.PayRsp
import com.sendinfo.wuzhizhou.entitys.response.PrintTempVo
import com.sendinfo.wuzhizhou.module.pay.contract.PayContract
import com.sendinfo.wuzhizhou.module.pay.presenter.PayPresenter
import com.sendinfo.wuzhizhou.utils.ClosePageAction
import com.sendinfo.wuzhizhou.utils.PayUtils
import com.sendinfo.wuzhizhou.utils.getTid
import com.sendinfo.wuzhizhou.utils.startActPrint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_pay.*

/**
 * 支付页面
 */
class PayActivity : BaseActivity<PayPresenter>(), PayContract.View {

    private lateinit var saveOrderVo: SaveOrderReq
    //    private var mid = "898469247330003"//商户号 旧的
    private var mid = "898460279994006"//商户号 新的
    private var tid = ""//终端号
    private var instMid = "QRPAYDEFAULT"//机构商户号(instMid)
    private var msgSrc = "WWW.AHUIBLHLY.COM"//消息来源
    private var msgSrcId = "4164"//来源编码
    private var secretKey = "Jac5shTTKS28xfiRKj72MzCKAPeepPSXpSTc4iKPwcMEjC3t"//秘钥
    private val handler = Handler()
    private var httpType = 0 // 0获取支付二维码 1查询支付结果 2撤销订单 3下订单
    private var countLoop = 2//当页面到计时或点击撤销订单或返回接口后 查询支付接口次数
    private val pageTime = 300 * 1000//页面倒计时5分钟
    private var billno = ""//订单号
    private var qrcodeID = ""//查询二维码返回的订单号

    override fun initArgs(intent: Intent?) {
        super.initArgs(intent)
        intent?.let {
            saveOrderVo = it.getSerializableExtra("saveOrderVo") as SaveOrderReq
        }
    }

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_pay)
        mPresenter = PayPresenter(this)
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        super.initData()
        tid = getTid()
        soundPoolUtils.startPlayVideo(R.raw.wxorzybsaomazhifu)

        tts.setIvLogo(R.drawable.ticketpurchase)
        tts.setBackVisibility(View.GONE)

        tvCount.text = "总数量：${saveOrderVo.TotalTicketCount}张"
        tvSumPrice.text = "总价格：${saveOrderVo.PaySum}元"
        if (TextUtils.isEmpty(billno)) {
            billno = PayUtils.genMerOrderId(msgSrcId)
            saveOrderVo.PayTradeId = billno
            mPresenter?.httpData(billno, mid, instMid, msgSrc, tid, saveOrderVo.PaySum, "", secretKey, 1)//获取二维码
            LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(ClosePageAction))//关闭其他页面广播
        }
    }

    /**
     * 获取支付二维码
     */
    override fun onGetQRCode(payRsp: PayRsp) {
        payQrCode(payRsp.billQRCode)
        qrcodeID = payRsp.qrCodeId
    }

    /**
     * 查询支付结果返回
     */
    override fun onQuery(payRsp: PayRsp) {
        if (payRsp.errCode == "SUCCESS") {
            if (payRsp.billStatus == "PAID") {
                tvPayInfo.text = "支付状态 : 支付成功"
                httpType = 3
                mPresenter?.saveOrder(saveOrderVo)//保存订单
                return
            }
        }
        tvPayInfo.text = "支付状态 : 支付中"
        trainQueryOrder()
    }

    /**
     * 关闭订单返回
     */
    override fun onCloseQRCode() {
        finish()
    }

    /**
     * 打印出票
     */
    override fun toPrintTemp(printTemp: MutableList<PrintTempVo>) {
        startActPrint(this, printTemp, "购票")
    }

    /**
     * 发起查询支付 或者撤销订单
     */
    override fun trainQueryOrder() {
        if (httpType == 2) {
            if (countLoop > 0) {
                countLoop--
                tvPayInfo.text = "支付状态 : 未支付"
                handler?.removeCallbacksAndMessages(null)
                handler?.postDelayed({
                    mPresenter?.httpData(
                        billno,
                        mid,
                        instMid,
                        msgSrc,
                        tid,
                        saveOrderVo.PaySum,
                        qrcodeID,
                        secretKey,
                        2
                    )//查询支付结果
                }, 1000)
            } else {
                mPresenter?.httpData(
                    billno,
                    mid,
                    instMid,
                    msgSrc,
                    tid,
                    saveOrderVo.PaySum,
                    qrcodeID,
                    secretKey,
                    3
                )//撤销订单
                tvPayInfo.text = "支付状态 : 订单撤销中"
            }
        } else {
            tvPayInfo.text = "支付状态 : 未支付"
            handler?.removeCallbacksAndMessages(null)
            handler?.postDelayed({
                mPresenter?.httpData(
                    billno,
                    mid,
                    instMid,
                    msgSrc,
                    tid,
                    saveOrderVo.PaySum,
                    qrcodeID,
                    secretKey,
                    2
                )//查询支付结果
            }, 1000)
        }
    }

    /**
     * 生成二维码
     */
    @SuppressLint("CheckResult", "AutoDispose")
    private fun payQrCode(url: String) {
        Observable
            .just(url)
            .map { s -> QRCodeEncoder.syncEncodeQRCode(s, BGAQRCodeUtil.dp2px(this, 270.0f)) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ bitmap ->
                if (bitmap == null) {
                    tvPayInfo?.text = "生成支付二维码失败"
                } else {
                    tts.setBackVisibility(View.VISIBLE)
                    tts.setBackOnClick(View.OnClickListener {
                        quTs()
                    })
                    tts.startSurplus(pageTime)
                    tts.setOnSurplusListener(object : OnSurplusListener {
                        override fun surplus() {
                            exitActivity()
                        }
                    })
                    ivQrCode.setImageBitmap(bitmap)
                    httpType = 1
                    trainQueryOrder()
                }
            }, { throwable -> throwable.printStackTrace() })
    }

    /**
     * 确定退出页面提示
     */
    private fun quTs() {
        if (httpType == 3) {
            showDialog("支付成功请勿退出")
        } else {
            showDialog(content = "确定退出请勿继续支付,点击取消继续操作?", confirmListener = OnConfirmListener {
                exitActivity()
            }, cancelListener = getCancelDisListener(), isHideCancel = false)
        }
    }

    /**
     * 退出页面，查询两次支付结果后，未支付调用撤销订单接口
     */
    private fun exitActivity() {
        showDialog("请等待...")
        if (httpType != 3) httpType = 2
    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacksAndMessages(null)
    }
}
