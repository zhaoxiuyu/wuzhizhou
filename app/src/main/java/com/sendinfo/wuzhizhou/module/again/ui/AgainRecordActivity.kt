package com.sendinfo.wuzhizhou.module.again.ui

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.module.again.adapter.ManagementRecordAdapter
import com.sendinfo.wuzhizhou.module.again.contract.AgainRecordContract
import com.sendinfo.wuzhizhou.module.again.presenter.AgainRecordPresenter
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.activity_again_record.*

/**
 * 查询交易记录
 */
class AgainRecordActivity : BaseActivity<AgainRecordContract.Presenter>(),
    AgainRecordContract.View, BaseQuickAdapter.OnItemChildClickListener {

    private val mAdapter: ManagementRecordAdapter by lazy { ManagementRecordAdapter() }

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_again_record)
        mPresenter = AgainRecordPresenter(this)
    }

    override fun initData() {
        super.initData()
        initAdapter()
    }

    /**
     * 初始化适配器
     */
    private fun initAdapter() {
        mAdapter.onItemChildClickListener = this
        mAdapter.emptyView = notDataView
        rv.layoutManager = LinearLayoutManager(this)
        rv.addItemDecoration(HorizontalDividerItemDecoration.Builder(this).build())
        rv.adapter = mAdapter
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }

    override fun checkSuccess(request: String?) {
    }

    override fun checkError(msg: String?) {
    }

}

