package com.sendinfo.wuzhizhou.module.purchase.ui

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.entitys.hardware.CardInfo
import com.sendinfo.wuzhizhou.entitys.request.IDCardsReq
import com.sendinfo.wuzhizhou.entitys.response.GetTicketVo
import com.sendinfo.wuzhizhou.interfaces.IdCardListener
import com.sendinfo.wuzhizhou.module.purchase.adapter.RealNameAdapter
import com.sendinfo.wuzhizhou.owner.IdCardOwner
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import kotlinx.android.synthetic.main.dialog_real_name.*

class RealNameDialog : DialogFragment(), BaseQuickAdapter.OnItemChildClickListener {

    private val mAdapter: RealNameAdapter by lazy { RealNameAdapter() }

    var ticketVo: GetTicketVo? = null

    var onClick: View.OnClickListener? = null

    var idCardRead = false // 让身份证一直阅读,用状态来判断是否处理,true可以处理,false不处理

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.dialog_real_name, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        butClose.setOnClickListener {
            dismiss()
            onClick?.onClick(it)
        }
        butDis.setOnClickListener {
            dismiss()
        }
    }

    /**
     * 初始化适配器
     */
    private fun initAdapter() {
        mAdapter.onItemChildClickListener = this
        rv.adapter = mAdapter
        rv.layoutManager = LinearLayoutManager(context)
        rv.addItemDecoration(HorizontalDividerItemDecoration.Builder(context).build())
    }

    override fun onStart() {
        super.onStart()
        //设置对话框dialog的高度和宽度
        val window = dialog.window
        val display = DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(display)

        val wlp = window.attributes
        wlp.gravity = Gravity.CENTER
        wlp.width = (display.widthPixels * 0.9f).toInt()
        wlp.height = (display.heightPixels * 0.9f).toInt()
        window.attributes = wlp

        isCancelable = false
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        mAdapter.remove(position)
    }

    // 读取身份证的回调
    fun verification(cardInfo: CardInfo) {
        if (!idCardRead) return

        var isAddIdCard = true // true 可以添加,false 身份证重复 不能添加
        val idCards = mAdapter.data

        if (idCards.size == ticketVo?.tvNumber) {
            tvInfo.text = "身份证已添加完成"
            return
        }

        idCards.forEach {
            if (it.FNumber.equals(cardInfo.card)) { // 身份证重复
                isAddIdCard = false
            }
        }

        if (!isAddIdCard) {
            tvInfo.text = "身份证不能重复"
            return
        }

        val idCardsReq = IDCardsReq()
        idCardsReq.FAddress = "${cardInfo.address?.trim()}"
        idCardsReq.FName = "${cardInfo.name?.trim()}"
        idCardsReq.FNumber = "${cardInfo.card?.trim()}"
        idCardsReq.FSex = "${cardInfo.sex?.trim()}"

        mAdapter.addData(idCardsReq)
    }

    override fun onResume() {
        idCardRead = true
        super.onResume()

        diaTicketModelName.text = "票型 : ${ticketVo?.TicketModelName}"
        diaTicketModelKindName.text = "票种 : ${ticketVo?.TicketModelKindName}"
        diaRebatePrice.text = "价格 : ${ticketVo?.RebatePrice}"
        diaNumber.text = "数量 : ${ticketVo?.tvNumber}"
        idCardNumber.text = "所需身份证数量 : ${ticketVo?.tvNumber}"

        mAdapter.setNewData(ticketVo?.IDCards)
    }

    override fun onPause() {
        idCardRead = false
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        lav?.cancelAnimation()
    }

}