package com.sendinfo.wuzhizhou.module.again.ui

import android.content.Intent
import com.base.library.database.DataBaseUtils
import com.base.library.mvp.BPresenter
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.module.common.ui.GestureActivity
import com.sendinfo.wuzhizhou.utils.*
import kotlinx.android.synthetic.main.activity_again_setting.*
import kotlinx.android.synthetic.main.activity_base.*

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
        tts.startSurplus(300 * 1000)
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
         * IP端口
         */
        etIp.setText(getIp())
        btIp.setOnClickListener {
            var ip = etIp.text.toString().trim()
            if (StringUtils.isEmpty(ip)) {
                ToastUtils.showShort("请输入IP端口")
                return@setOnClickListener
            }
            if (!ip.startsWith("http:") && !ip.startsWith("https:")) ip = "http://$ip/"
            putIp(ip)
            ToastUtils.showShort("保存成功")
        }

        /**
         * 一次取票数最大限制
         */
        etTakeNumber.setText("${getTakeNumber()}")
        butTakeNumber.setOnClickListener {
            val takeNumber = etTakeNumber.text.toString()
            if (StringUtils.isEmpty(takeNumber)) {
                ToastUtils.showShort("请输入一次取票数最大限制,不能为空")
                return@setOnClickListener
            }
            putTakeNumber(takeNumber.toInt())
            ToastUtils.showShort("保存成功")
        }

        //测试打印机
        testDyj.setOnClickListener {
            //            var printList = ArrayList<PrintTempVo>()
//            printList.add(PrintTempVo().apply {
//                PrintTemp = defaultTemplate()
//            })
            startAct(this, TestPrintActivity::class.java, isFinish = false)
//            startActPrint(this, printList, "测试打印机")
        }

        //退出程序
        tuichu.setOnClickListener {
            showDialog(content = "确定要退出?", confirmListener = OnConfirmListener {
                DataBaseUtils.close()
                AppUtils.exitApp()
            })
        }

        // 支付参数设置
        tvPayParam.setOnClickListener {
            val intent = Intent(this, GestureActivity::class.java)
            intent.putExtra("source", payParam)
            startAct(this, intent, isFinish = false)
        }

        // 修改身份证设置
        tvUpdateIdCard.setOnClickListener {
            val intent = Intent(this, GestureActivity::class.java)
            intent.putExtra("source", updateIdCard)
            startAct(this, intent, isFinish = false)
        }
    }

}
