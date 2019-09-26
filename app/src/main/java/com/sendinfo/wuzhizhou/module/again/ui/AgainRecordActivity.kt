package com.sendinfo.wuzhizhou.module.again.ui

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.entitys.response.PrintTempVo
import com.sendinfo.wuzhizhou.entitys.response.QueryTradeVo
import com.sendinfo.wuzhizhou.module.again.adapter.ManagementRecordAdapter
import com.sendinfo.wuzhizhou.module.again.contract.AgainRecordContract
import com.sendinfo.wuzhizhou.module.again.presenter.AgainRecordPresenter
import com.sendinfo.wuzhizhou.utils.startActPrint
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.activity_again_record.*
import kotlinx.android.synthetic.main.activity_base.*

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
        tts.startSurplus(120000)

        initAdapter()
        btSubmit.setOnClickListener { toQueryTrade() }
        btSubmit.performClick()

    }

    /**
     * 初始化适配器
     */
    private fun initAdapter() {
        rv.layoutManager = LinearLayoutManager(this)
        rv.addItemDecoration(HorizontalDividerItemDecoration.Builder(this).build())
        rv.adapter = mAdapter
        mAdapter.onItemChildClickListener = this
    }

    // 查询重打列表
    private fun toQueryTrade() {
        val tradeId = etTradeId.text.toString()
        val certNo = etCertNo.text.toString()

        //身份证不为空的情况下 并且 符合三个正则表达式其中的一个就可以了
        if (!StringUtils.isEmpty(certNo)) {
            if (!RegexUtils.isIDCard15(certNo) &&
                !RegexUtils.isIDCard18(certNo) &&
                !RegexUtils.isIDCard18Exact(certNo)
            ) {
                YoYo.with(Techniques.Shake).playOn(etCertNo)
                ToastUtils.showLong("身份证号码错误")
                return
            }
        }
        mPresenter?.queryTrade(tradeId, certNo)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val queryTradeVo = mAdapter.getItem(position)
        queryTradeVo?.Barcode?.let { mPresenter?.reprintTicket(it) }
    }

    override fun queryTradeSuccess(queryTradeVo: MutableList<QueryTradeVo>) {
        mAdapter.setNewData(queryTradeVo)
    }

    override fun reprintTicketSuccess(printTemp: MutableList<PrintTempVo>) {
        startActPrint(this, printTemp, "重打")
    }

}