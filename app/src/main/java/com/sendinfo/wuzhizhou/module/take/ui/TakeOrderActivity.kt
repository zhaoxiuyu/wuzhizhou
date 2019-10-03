package com.sendinfo.wuzhizhou.module.take.ui

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.library.util.isFastClick
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.entitys.response.TakeOrderModelsVo
import com.sendinfo.wuzhizhou.entitys.response.TakeTicketModelsVo
import com.sendinfo.wuzhizhou.module.take.adapter.TakeOrderAdapter
import com.sendinfo.wuzhizhou.module.take.contract.TakeOrderInfoContract
import com.sendinfo.wuzhizhou.module.take.presenter.TakeOrderInfoPresenter
import com.sendinfo.wuzhizhou.utils.TakeOrderInfo4
import com.sendinfo.wuzhizhou.utils.startActTakeDetailed
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_take_order.*
import java.util.*

/**
 * 取票订单
 */
class TakeOrderActivity : BaseActivity<TakeOrderInfoContract.Presenter>(), TakeOrderInfoContract.View,
    BaseQuickAdapter.OnItemChildClickListener {

    private val mAdapter: TakeOrderAdapter by lazy { TakeOrderAdapter() }
    private var takeOrderModels: MutableList<TakeOrderModelsVo>? = null

    private val uuid: String by lazy { UUID.randomUUID().toString().replace("-", "") }

    override fun initArgs(intent: Intent?) {
        super.initArgs(intent)
        intent?.let {
            takeOrderModels = it.getSerializableExtra("takeOrderModels") as MutableList<TakeOrderModelsVo>
        }
    }

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_take_order)
        tts.setIvLogo(R.drawable.tickettake)
        mPresenter = TakeOrderInfoPresenter(this)
    }

    override fun initData() {
        super.initData()
        soundPoolUtils.startPlayVideo(R.raw.order)

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
        mAdapter.setNewData(takeOrderModels)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        if (isFastClick()) return

        val takeTicketModelsVo = mAdapter.getItem(position)
        val billNo = takeTicketModelsVo?.BillNo ?: ""
        if (StringUtils.isEmpty(billNo)) {
            ToastUtils.showShort("该订单没有订单编号")
        } else {
            mPresenter?.getTakeOrderInfo(TakeOrderInfo4, billNo, uuid, false)
        }
    }

    override fun toOrder(takeOrderModels: MutableList<TakeOrderModelsVo>) {
    }

    override fun toDetailed(takeTicketModels: MutableList<TakeTicketModelsVo>) {
        startActTakeDetailed(this, uuid, takeTicketModels)
    }

}
