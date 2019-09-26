package com.sendinfo.wuzhizhou.module.purchase.ui

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.library.mvp.BPresenter
import com.base.library.util.ArithMultiply
import com.base.library.util.JsonUtils
import com.base.library.util.isFastClick
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.entitys.request.SaveOrderReq
import com.sendinfo.wuzhizhou.entitys.request.TicketInfosReq
import com.sendinfo.wuzhizhou.entitys.response.GetTicketVo
import com.sendinfo.wuzhizhou.module.pay.ui.PayTypeActivity
import com.sendinfo.wuzhizhou.module.purchase.adapter.PurchaseSureAdapter
import com.sendinfo.wuzhizhou.owner.IdCardOwner
import com.sendinfo.wuzhizhou.utils.getShebeiCode
import com.sendinfo.wuzhizhou.utils.startAct
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_purchase_main.*

/**
 * 确定购买票型
 */
class PurchaseSureActivity : BaseActivity<BPresenter>(), BaseQuickAdapter.OnItemChildClickListener {

    private val mAdapter: PurchaseSureAdapter by lazy { PurchaseSureAdapter() }
    private val mDialog: RealNameDialog by lazy { RealNameDialog() }
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
        lifecycle.addObserver(idCardOwner)
    }

    override fun initData() {
        super.initData()
        soundPoolUtils.startPlayVideo(R.raw.idcarname)
        mDialog.setIdCardOwner(idCardOwner)

        tts.startSurplus(120000)
        btSubmit.setOnClickListener {
            if (isFastClick()) return@setOnClickListener
            saveOrderData()
        }
        initAdapter()
    }

    /**
     * 初始化适配器
     */
    private fun initAdapter() {
        rv.layoutManager = LinearLayoutManager(this)
        rv.addItemDecoration(HorizontalDividerItemDecoration.Builder(this).build())
        rv.adapter = mAdapter
        mAdapter.onItemChildClickListener = this
        mAdapter.setNewData(newTickets)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        if (isFastClick()) return
        val ticketVo = mAdapter.getItem(position)

        mDialog.show(supportFragmentManager, "RealNameDialog")
        if (ticketVo?.IDCards == null) ticketVo?.IDCards = mutableListOf()
        mDialog.setNewData(ticketVo?.IDCards, ticketVo?.tvNumber ?: 0)
    }

    /**
     * 验证数据是否合法并组装好下单数据
     */
    private fun saveOrderData() {
        val ticketVos = mAdapter.data
        ticketVos?.forEach {
            if (it.IDCards.isNullOrEmpty()) {
                ToastUtils.showShort("请刷身份证进行实名认证")
                return
            }
            if (it.tvNumber <= 0) {
                ToastUtils.showShort("票数不能小于等于0,请返回重新选择")
                return
            }
            // 购票数和身份证数量不相等
            if (it.tvNumber != it.IDCards?.size) {
                ToastUtils.showShort("实名认证数量不合法")
                return
            }
        }

        var sum = 0.0 // 总金额
        var count = 0 // 总数量
        var ticketInfoVos = mutableListOf<TicketInfosReq>()
        ticketVos?.forEach {
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

        val saveOrderVo = SaveOrderReq()
        saveOrderVo.OptType = "0"
        saveOrderVo.TerminalCode = getShebeiCode()
        saveOrderVo.PayTypeCode = "07"
        saveOrderVo.PayTypeName = "电子商务"
        saveOrderVo.PaySum = sum
        saveOrderVo.TotalTicketCount = count
        saveOrderVo.BillNo = ""
        saveOrderVo.LockGuid = ""
        saveOrderVo.TicketInfos = ticketInfoVos

        LogUtils.json(JsonUtils.toJson(saveOrderVo))
        startAct(this, PayTypeActivity::class.java)
    }

}
