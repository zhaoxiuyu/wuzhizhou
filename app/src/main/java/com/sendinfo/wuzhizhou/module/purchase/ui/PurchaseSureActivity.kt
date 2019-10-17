package com.sendinfo.wuzhizhou.module.purchase.ui

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.library.interfaces.OnSurplusListener
import com.base.library.mvp.BPresenter
import com.base.library.util.ArithMultiply
import com.base.library.util.JsonUtils
import com.base.library.util.isFastClick
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.entitys.hardware.CardInfo
import com.sendinfo.wuzhizhou.entitys.request.SaveOrderReq
import com.sendinfo.wuzhizhou.entitys.request.TicketInfosReq
import com.sendinfo.wuzhizhou.entitys.response.GetTicketVo
import com.sendinfo.wuzhizhou.interfaces.IdCardListener
import com.sendinfo.wuzhizhou.module.pay.ui.PayTypeActivity
import com.sendinfo.wuzhizhou.module.purchase.adapter.PurchaseSureAdapter
import com.sendinfo.wuzhizhou.owner.IdCardOwner
import com.sendinfo.wuzhizhou.utils.getPrintNumber
import com.sendinfo.wuzhizhou.utils.getShebeiCode
import com.sendinfo.wuzhizhou.utils.startAct
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_purchase_main.*

/**
 * 确定购买票型
 */
class PurchaseSureActivity : BaseActivity<BPresenter>(), BaseQuickAdapter.OnItemChildClickListener,
    View.OnClickListener, IdCardListener {

    private val mAdapter: PurchaseSureAdapter by lazy { PurchaseSureAdapter() }
    private var mDialog: RealNameDialog? = null
    private var newTickets: MutableList<GetTicketVo>? = null
    private val idCardOwner: IdCardOwner by lazy { IdCardOwner(this) }

    override fun initArgs(intent: Intent?) {
        super.initArgs(intent)
        intent?.let {
            newTickets = it.getSerializableExtra("newTickets") as MutableList<GetTicketVo>
        }
    }

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_purchase_sure)
        tts.setIvLogo(R.drawable.ticketpurchase)
        lifecycle.addObserver(idCardOwner)
    }

    override fun initData() {
        super.initData()
        soundPoolUtils.startPlayVideo(R.raw.idcarname)
        tts.startSurplus(120000)
        tts.setOnSurplusListener(object : OnSurplusListener {
            override fun surplus() {
                disDia()
            }
        })
        tts.setBackOnClick(View.OnClickListener {
            disDia()
        })
        btSubmit.setOnClickListener {
            if (isFastClick()) return@setOnClickListener
            saveOrderData()
        }
        initAdapter()

        mDialog = RealNameDialog()
        mDialog?.onClick = this
        idCardOwner.setIdCardListener(this)
        idCardOwner?.getReadIdCard()
    }

    /**
     * 初始化适配器
     */
    private fun initAdapter() {
        rv.layoutManager = LinearLayoutManager(this)
        rv.addItemDecoration(HorizontalDividerItemDecoration.Builder(this).build())
        mAdapter.bindToRecyclerView(rv)
        mAdapter.onItemChildClickListener = this
        mAdapter.setNewData(newTickets)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val ticketVo = mAdapter.getItem(position) as GetTicketVo
        if (ticketVo.NeedReadIDCard == "1") {
            if (ticketVo?.IDCards == null) ticketVo?.IDCards = mutableListOf()

            mDialog?.ticketVo = ticketVo
            mDialog?.show(supportFragmentManager, "RealNameDialog")
        }
    }

    // 身份证读取的回调
    override fun idCardListener(cardInfo: CardInfo) {
        mDialog?.verification(cardInfo)
        idCardOwner?.getReadIdCard()
    }

    // 点击 确定 关闭对话框的回调
    override fun onClick(v: View?) {
        saveOrderData()
    }

    /**
     * 验证数据是否合法并组装好下单数据
     */
    private fun saveOrderData() {
        for ((index, vo) in newTickets?.withIndex()!!) {
            if (vo.tvNumber <= 0) {
                ToastUtils.showShort("票数不能小于等于0,请返回重新选择")
                return
            }

            // 身份证数量为空
            if (vo.NeedReadIDCard == "1" && vo.IDCards.isNullOrEmpty()) {
                automaticDialog(index)
                return
            }

            // 购票数和身份证数量不相等
            if (vo.NeedReadIDCard == "1" && vo.tvNumber != vo.IDCards?.size) {
                automaticDialog(index)
                return
            }
        }

        var sum = 0.0 // 总金额
        var count = 0 // 总数量
        var ticketInfoVos = mutableListOf<TicketInfosReq>()
        newTickets?.forEach {
            // 每个票型
            val ticketInfoVo = TicketInfosReq()
            ticketInfoVo.BillDetailNo = ""
            ticketInfoVo.TicketModelCode = it.TicketModelCode
            ticketInfoVo.TicketModelName = it.TicketModelName
            ticketInfoVo.TicketModelPrice = it.RebatePrice
            ticketInfoVo.TicketCount = it.tvNumber
            ticketInfoVo.IDCards = it.IDCards
            ticketInfoVos.add(ticketInfoVo)

            // 累加取票数量
            count += it.tvNumber

            // 总价格 = 数量 * 价格
            sum += ArithMultiply(it.tvNumber.toDouble(), it.RebatePrice ?: 0.0)
        }

        if (getPrintNumber() < count) {
            ToastUtils.showShort("可打印票纸数量不足，请联系景区管理人员")
            soundPoolUtils.startPlayVideo(R.raw.piaozhibuzu)
            return
        }

        val saveOrderVo = SaveOrderReq()
        saveOrderVo.OptType = "0"
        saveOrderVo.TerminalCode = getShebeiCode()
        saveOrderVo.PayTypeCode = "56"
        saveOrderVo.PayTypeName = "银联聚合支付"
        saveOrderVo.PaySum = sum
        saveOrderVo.TotalTicketCount = count
        saveOrderVo.BillNo = ""
        saveOrderVo.LockGuid = ""
        saveOrderVo.PayTradeId = ""//支付订单号，支付页面生成
        saveOrderVo.TicketInfos = ticketInfoVos

        LogUtils.json(JsonUtils.toJson(saveOrderVo))
        startAct(
            this,
            Intent(this, PayTypeActivity::class.java).putExtra("saveOrderVo", saveOrderVo)
        )
//        startAct(this, Intent(this, PayActivity::class.java).putExtra("saveOrderVo", saveOrderVo))
    }

    // 需要验证身份证,自动弹出框
    private fun automaticDialog(index: Int) {
        ToastUtils.showShort("身份证数量不合法,请刷身份证进行实名认证")
        val btIdCard = mAdapter?.getViewByPosition(index, R.id.btIdCard)
        btIdCard?.performClick()
    }

    private fun disDia() {
        mDialog?.dismiss()
        this@PurchaseSureActivity.finish()
    }

}
