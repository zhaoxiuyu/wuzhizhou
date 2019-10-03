package com.sendinfo.wuzhizhou.module.take.ui

import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.entitys.hardware.CardInfo
import com.sendinfo.wuzhizhou.entitys.response.TakeOrderModelsVo
import com.sendinfo.wuzhizhou.entitys.response.TakeTicketModelsVo
import com.sendinfo.wuzhizhou.interfaces.IdCardListener
import com.sendinfo.wuzhizhou.module.take.contract.TakeOrderInfoContract
import com.sendinfo.wuzhizhou.module.take.presenter.TakeOrderInfoPresenter
import com.sendinfo.wuzhizhou.owner.IdCardOwner
import com.sendinfo.wuzhizhou.utils.TakeOrderInfo1
import com.sendinfo.wuzhizhou.utils.startActTakeDetailed
import com.sendinfo.wuzhizhou.utils.startActTakeOrder
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_take_idcard.*
import java.util.*

/**
 * 身份证
 */
class TakeIdCardActivity : BaseActivity<TakeOrderInfoContract.Presenter>(), TakeOrderInfoContract.View, IdCardListener {

    private val uuid: String by lazy { UUID.randomUUID().toString().replace("-", "") }
    private val idCardOwner: IdCardOwner by lazy { IdCardOwner(this) }

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_take_idcard)
        tts.setIvLogo(R.drawable.tickettake)
        mPresenter = TakeOrderInfoPresenter(this)
        lifecycle.addObserver(idCardOwner)
    }

    override fun initData() {
        super.initData()
        soundPoolUtils.startPlayVideo(R.raw.idcard)

        idCardOwner.setIdCardListener(this)
        idCardOwner.getReadIdCard()
    }

    override fun idCardListener(cardInfo: CardInfo) {
        mPresenter?.getTakeOrderInfo(TakeOrderInfo1, "${cardInfo.card}", uuid)
    }

    override fun toOrder(takeOrderModels: MutableList<TakeOrderModelsVo>) {
        startActTakeOrder(this, takeOrderModels)
    }

    override fun toDetailed(takeTicketModels: MutableList<TakeTicketModelsVo>) {
        startActTakeDetailed(this, uuid, takeTicketModels)
    }

    override fun onDestroy() {
        super.onDestroy()
        lav.cancelAnimation()
    }

}
