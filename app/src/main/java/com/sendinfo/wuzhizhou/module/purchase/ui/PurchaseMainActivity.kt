package com.sendinfo.wuzhizhou.module.purchase.ui

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.library.util.isFastClick
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.entitys.response.GetTicketGroupVo
import com.sendinfo.wuzhizhou.entitys.response.GetTicketVo
import com.sendinfo.wuzhizhou.module.purchase.adapter.PurchaseMainAdapter
import com.sendinfo.wuzhizhou.module.purchase.adapter.PurchaseMainGroupAdapter
import com.sendinfo.wuzhizhou.module.purchase.contract.PurchaseMainContract
import com.sendinfo.wuzhizhou.module.purchase.presenter.PurchaseMainPresenter
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_purchase_main.*
import java.io.Serializable

/**
 * 购票,票型列表
 */
class PurchaseMainActivity : BaseActivity<PurchaseMainContract.Presenter>(), PurchaseMainContract.View,
    BaseQuickAdapter.OnItemChildClickListener {

    private var tickets: MutableList<GetTicketVo>? = null
    private val mTicketsAdapter: PurchaseMainAdapter by lazy { PurchaseMainAdapter() }

    private val mGroupAdapter: PurchaseMainGroupAdapter by lazy { PurchaseMainGroupAdapter() }

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_purchase_main)
        mPresenter = PurchaseMainPresenter(this)
    }

    override fun initData() {
        super.initData()
        soundPoolUtils.startPlayVideo(R.raw.purchase)

        initAdapter()
        tts.startSurplus(120000)
        btSubmit.setOnClickListener {
            if (isFastClick()) return@setOnClickListener
            toAddIdCard()
        }

        mPresenter?.getTicket()
    }

    /**
     * 初始化适配器
     */
    private fun initAdapter() {
        mGroupAdapter.onItemChildClickListener = groupClick
        mGroupAdapter.bindToRecyclerView(rvGroup)
        rvGroup.layoutManager = GridLayoutManager(this, 4)

        mTicketsAdapter.onItemChildClickListener = this
        rv.adapter = mTicketsAdapter
        rv.layoutManager = LinearLayoutManager(this)
        rv.addItemDecoration(HorizontalDividerItemDecoration.Builder(this).build())
    }

    /**
     * 分组点击事件
     */
    private val groupClick = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
        val groupItem = mGroupAdapter.getItem(position)

        // 设置分组选中
        mGroupAdapter.data.forEach { it.isSelection = false }
        groupItem?.isSelection = true

        // 如果分组里面不包含票型列表,就用分组编码把过滤的票型赋值给分组里面的票型集合,
        if (groupItem?.tickets == null) {
            groupItem?.tickets =
                tickets?.filter { it.TicketGroup.equals(groupItem?.GroupCode) } as MutableList<GetTicketVo>
        }

        mGroupAdapter.notifyDataSetChanged()
        mTicketsAdapter.setNewData(groupItem?.tickets)
    }

    /**
     * 票型点击事件
     */
    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val ticket = mTicketsAdapter.getItem(position)
        var number = ticket?.tvNumber ?: 0
        when (view?.id) {
            R.id.tvAdd -> number += 1
            R.id.tvRemove -> if (number > 0) number -= 1
        }
        ticket?.tvNumber = number
        mTicketsAdapter.notifyItemChanged(position)
    }

    /**
     * 分组回调
     */
    override fun getTicketGroupSuccess(ticketGroup: MutableList<GetTicketGroupVo>) {
        mGroupAdapter.setNewData(ticketGroup)
        // 默认选中第一个分组
        mHandler.post { mGroupAdapter.getViewByPosition(0, R.id.tvGroup)?.performClick() }
    }

    /**
     * 票型回调
     */
    override fun getTicketSuccess(tickets: MutableList<GetTicketVo>) {
        this.tickets = tickets
    }

    private fun toAddIdCard() {
        // 判断该分组下面是否有票型
        val ticketVos = mTicketsAdapter.data
        if (ticketVos.isNullOrEmpty()) {
            ToastUtils.showShort("没有选择票型")
            return
        }

        // 选择了数量的票型添加到新集合里面
        val newTickets = mutableListOf<GetTicketVo>()
        ticketVos?.forEach {
            if (it.tvNumber > 0) newTickets.add(it)
        }

        // 判断有没有选择票型数量(新集合有没有票型)
        if (newTickets.isNullOrEmpty()) {
            ToastUtils.showShort("没有选择票型")
            return
        }

        val intent = Intent(this, PurchaseSureActivity::class.java)
        intent.putExtra("newTickets", newTickets as Serializable)
        startActivity(intent)
        this@PurchaseMainActivity.finish()

    }

}
