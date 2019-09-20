package com.sendinfo.wuzhizhou.module.take.ui

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.library.util.isFastClick
import com.chad.library.adapter.base.BaseQuickAdapter
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.entitys.response.TakeTicketModelsVo
import com.sendinfo.wuzhizhou.module.take.adapter.TakeDetailedAdapter
import com.sendinfo.wuzhizhou.module.take.contract.TakeDetailedContract
import com.sendinfo.wuzhizhou.module.take.presenter.TakeDetailedPresenter
import com.sendinfo.wuzhizhou.utils.startActPrint
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.activity_take_detailed.*

/**
 * 取票明细
 */
class TakeDetailedActivity : BaseActivity<TakeDetailedContract.Presenter>(), TakeDetailedContract.View,
    BaseQuickAdapter.OnItemChildClickListener {

    private val mAdapter: TakeDetailedAdapter by lazy { TakeDetailedAdapter() }
    private var takeTicketModels: MutableList<TakeTicketModelsVo>? = null

    private var uuid: String? = null

    override fun initArgs(intent: Intent?) {
        super.initArgs(intent)
        intent?.let {
            uuid = it.getStringExtra("uuid")
            takeTicketModels = it.getSerializableExtra("takeTicketModels") as MutableList<TakeTicketModelsVo>
        }
    }

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_take_detailed)
        mPresenter = TakeDetailedPresenter(this)
    }

    override fun initData() {
        super.initData()
        initAdapter()

        // 取票保存订单
        btSubmit.setOnClickListener {
            if (isFastClick()) return@setOnClickListener
            mPresenter?.saveOrder(takeTicketModels, uuid ?: "")
        }
    }

    /**
     * 初始化适配器
     */
    private fun initAdapter() {
        rv.layoutManager = LinearLayoutManager(this)
        rv.addItemDecoration(HorizontalDividerItemDecoration.Builder(this).build())
        rv.adapter = mAdapter
        mAdapter.onItemChildClickListener = this
        mAdapter.setNewData(takeTicketModels)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val ticketModelsVo = mAdapter.getItem(position)
        val ticketNum = ticketModelsVo?.TicketNum ?: 0
        var number = ticketModelsVo?.number ?: 0

        when (view?.id) {
            // 已选数量 小于票数 才能累加
            R.id.tvAdd -> if (number < ticketNum) number += 1
            R.id.tvRemove -> if (number > 0) number -= 1
        }

        ticketModelsVo?.number = number
        mAdapter.notifyItemChanged(position)
    }

    override fun toPrintTemp(printTemp: MutableList<String>) {
        startActPrint(this, printTemp)
    }

}
