package com.sendinfo.wuzhizhou.module.take.ui

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.library.entitys.BaseResponse
import com.base.library.mvp.BPresenter
import com.base.library.mvp.BaseView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.module.take.adapter.TakeOrderAdapter
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.activity_take_order.*

/**
 * 取票订单
 */
class TakeOrderActivity : BaseActivity<BPresenter>(), BaseView, BaseQuickAdapter.OnItemChildClickListener {

    private var mAdapter: TakeOrderAdapter? = null

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_take_order)
    }

    override fun initData() {
        super.initData()
        initAdapter()
    }

    /**
     * 初始化适配器
     */
    private fun initAdapter() {
        mAdapter = TakeOrderAdapter()
        mAdapter?.onItemChildClickListener = this
        mAdapter?.emptyView = notDataView
        rv.layoutManager = LinearLayoutManager(this)
        rv.addItemDecoration(HorizontalDividerItemDecoration.Builder(this).build())
        rv.adapter = mAdapter
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun bindData(baseResponse: BaseResponse) {
    }

    override fun bindError(string: String) {
    }

}
