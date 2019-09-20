package com.sendinfo.wuzhizhou.module.purchase.ui

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.library.mvp.BPresenter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.entitys.response.GetTicketVo
import com.sendinfo.wuzhizhou.module.purchase.adapter.PurchaseSureAdapter
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.activity_purchase_main.*

/**
 * 确定购买票型
 */
class PurchaseSureActivity : BaseActivity<BPresenter>(), BaseQuickAdapter.OnItemChildClickListener {

    private val mAdapter: PurchaseSureAdapter by lazy { PurchaseSureAdapter() }
    var newTickets: MutableList<GetTicketVo>? = null

    override fun initArgs(intent: Intent?) {
        super.initArgs(intent)
        intent?.let {
            newTickets = it.getSerializableExtra("newTickets") as MutableList<GetTicketVo>
        }
    }

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_purchase_sure)
    }

    override fun initData() {
        super.initData()
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

    }

}
