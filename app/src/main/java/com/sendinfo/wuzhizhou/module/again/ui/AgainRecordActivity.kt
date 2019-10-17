package com.sendinfo.wuzhizhou.module.again.ui

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.library.util.isFastClick
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.lxj.xpopup.XPopup
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.entitys.response.PrintTempVo
import com.sendinfo.wuzhizhou.entitys.response.QueryTradeVo
import com.sendinfo.wuzhizhou.module.again.adapter.ManagementRecordAdapter
import com.sendinfo.wuzhizhou.module.again.contract.AgainRecordContract
import com.sendinfo.wuzhizhou.module.again.presenter.AgainRecordPresenter
import com.sendinfo.wuzhizhou.utils.getPrintNumber
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
        val assistCheckNo = etAssistCheckNo.text.toString()

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
        mPresenter?.queryTrade(tradeId, certNo, assistCheckNo)
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        if (isFastClick()) return
        val queryTradeVo = mAdapter.getItem(position)
        when (view?.id) {
            R.id.butAgain -> {
                //打印纸票数不足,请管理员重新设置
                if (getPrintNumber() < 1) {
                    ToastUtils.showShort("可打印票纸数量不足，请联系景区管理人员")
                    soundPoolUtils.startPlayVideo(R.raw.piaozhibuzu)
                    return
                }
                queryTradeVo?.Barcode?.let { mPresenter?.reprintTicket(it) }
            }
            R.id.butChaKan -> {
                var name = queryTradeVo?.IDCardInfo?.FName ?: "非实名制"
                name = if (name == "非实名制") {
                    "购票人:$name"
                } else {
                    "购票人:$name  ||  身份证号码:${queryTradeVo?.IDCardInfo?.FNumber}"
                }

                var arr = arrayOf<String>()
                queryTradeVo?.ParkInfos?.forEach {
                    arr = arr.plus(it.toString()) // plus就是拷贝了原来的数组并且添加新元素,返回一个新数组
                }

                XPopup.Builder(getContext())
                    .asCenterList(name, arr) { position, text -> }.show()

//                XPopup.Builder(getContext())
//                    .asCenterList("购票人:$name", arrayOf("条目1", "条目2", "条目3", "条目4")) { position, text -> }.show()
            }
        }
    }

    override fun queryTradeSuccess(queryTradeVo: MutableList<QueryTradeVo>) {
        mAdapter.setNewData(queryTradeVo)
    }

    override fun reprintTicketSuccess(printTemp: MutableList<PrintTempVo>) {
        startActPrint(this, printTemp, "重打")
    }

}