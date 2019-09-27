package com.sendinfo.wuzhizhou.module.again.ui

import com.base.library.database.DataBaseUtils
import com.base.library.mvp.BPresenter
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.utils.*
import kotlinx.android.synthetic.main.activity_again_setting.*

/**
 * 设置
 */
class AgainSettingActivity : BaseActivity<BPresenter>() {

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_again_setting)
    }

    override fun initData() {
        super.initData()

        /**
         * 票数
         */
        etPrintNumber.setText(getPrintNumber().toString())
        btPrintNumber.setOnClickListener {
            val printNumber = etPrintNumber.text.toString()
            if (StringUtils.isEmpty(printNumber)) {
                ToastUtils.showShort("请输入可打印票数")
                return@setOnClickListener
            }
            putPrintNumber(printNumber.toInt())
            ToastUtils.showShort("保存成功")
        }

        /**
         * 1 霍尼打印机,2 TSC打印机
         */
        if (getDyj() == 1) {
            radioGroupDyj.check(weierDyj.id)
        } else {
            radioGroupDyj.check(tscDyj.id)
        }
        radioGroupDyj.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == weierDyj.id) {
                putDyj(1)
            } else if (checkedId == tscDyj.id) {
                putDyj(2)
            }
        }

        /**
         * 1 霍尼,2 HID ,身份证阅读器
         */
        if (getIdCard() == 1) {
            radioGroupIdCard.check(weier.id)
        } else {
            radioGroupIdCard.check(hid.id)
        }
        radioGroupIdCard.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == weier.id) {
                putIdCard(1)
            } else if (checkedId == hid.id) {
                putIdCard(2)
            }
        }

        /**
         * IP端口
         */
        etIp.setText(getIp())
        btIp.setOnClickListener {
            val ip = etIp.text.toString()
            if (StringUtils.isEmpty(ip)) {
                ToastUtils.showShort("请输入IP端口")
                return@setOnClickListener
            }
            putIp(ip)
        }

        //测试打印机
        testDyj.setOnClickListener { }

        //退出程序
        tuichu.setOnClickListener {
            showDialog(content = "确定要退出?", confirmListener = OnConfirmListener {
                DataBaseUtils.close()
                AppUtils.exitApp()
            })
        }

    }

}