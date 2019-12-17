package com.sendinfo.wuzhizhou.owner

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import androidx.lifecycle.LifecycleOwner
import com.base.library.interfaces.MyLifecycleObserver
import com.blankj.utilcode.util.*
import com.sdt.Common
import com.sdt.Sdtapi
import com.sendinfo.wuzhizhou.entitys.hardware.CardInfo
import com.sendinfo.wuzhizhou.entitys.hardware.IdCardMsg
import com.sendinfo.wuzhizhou.entitys.hardware.ReadBaseMsgToStr
import com.sendinfo.wuzhizhou.interfaces.IdCardListener
import com.sendinfo.wuzhizhou.utils.HardwareExample
import com.sendinfo.wuzhizhou.utils.getIdCard
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.wellcom.GSdtUtil
import com.wellcom.IdCardCallback
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 身份证阅读器
 */
class IdCardOwner(private var context: Context?) : MyLifecycleObserver {

    private lateinit var owner: LifecycleOwner

    @Volatile
    private var weFlag = false
    @Volatile
    private var hidFlag = false

    private var sdta: Sdtapi? = null
    private var common: Common? = null
    private var myBroadcastReceiver: MyBroadcastReceiver? = null
    private val bGSdtUtil: GSdtUtil = HardwareExample.bGSdtUtil

    private var idCardListener: IdCardListener? = null

    override fun onCreate(owner: LifecycleOwner) {
        this.owner = owner
    }

    override fun onDestroy(owner: LifecycleOwner) {
        stopReadIdCard()
        myBroadcastReceiver?.let {
            ActivityUtils.getActivityByContext(context)?.unregisterReceiver(it)
        }
        idCardListener = null
        common = null
        sdta = null
        context = null
        myBroadcastReceiver = null
    }

    /**
     * 1 维尔,2 HID
     */
    fun setIdCardListener(idCardListener: IdCardListener) {
        this.idCardListener = idCardListener
    }

    fun getReadIdCard() {
        if (getIdCard() == 1) {
            getReadWeIdCard()
        } else {
            getReadHIDIdCard()
        }
    }

    private fun getReadWeIdCard() {
        if (common == null) common = Common()
        if (sdta == null) {
            try {
                sdta = Sdtapi(ActivityUtils.getActivityByContext(context))
            } catch (el: Exception) {
                ToastUtils.showLong("USB设备异常或无连接")
            }
        }

        if (myBroadcastReceiver == null) {
            myBroadcastReceiver = MyBroadcastReceiver()

            val filter = IntentFilter()// 意图过滤器
            filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)// USB设备拔出
            filter.addAction(common?.ACTION_USB_PERMISSION)// 自定义的USB设备请求授权
            ActivityUtils.getActivityByContext(context).registerReceiver(myBroadcastReceiver, filter)
        }

        weFlag = true
        Observable.create<IdCardMsg> {
            while (weFlag) {
                sdta?.SDT_StartFindIDCard() // 寻找身份证
                sdta?.SDT_SelectIDCard() // 选取身份证

                val idCardMsg = IdCardMsg() // 身份证信息对象,存储身份证上的文字信息
                val ret = sdta?.let { ReadBaseMsgToStr(idCardMsg, it) }

                if (ret == 0x90 && !StringUtils.isEmpty(idCardMsg.id_num)) {
                    weFlag = false
                    it.onNext(idCardMsg)
                    it.onComplete()
                }
            }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner)))
            .subscribe({
                val cardInfo = CardInfo()
                cardInfo.address = it.address?.trim()
                cardInfo.card = it.id_num?.trim()
                cardInfo.name = it.name?.trim()
                cardInfo.nation = it.nation_str?.trim()
                cardInfo.sex = it.sex?.trim()
                cardInfo.bm = it.ptoto
                idCardListener?.idCardListener(cardInfo)
            }, { LogUtils.e("读取身份证出错了") })
    }

    private fun getReadHIDIdCard() {
        hidFlag = true
        Observable.create<com.wellcom.IdCardMsg> {
            while (hidFlag) {
                val idCardCallback = object : IdCardCallback {
                    override fun openSucc(p0: com.wellcom.IdCardMsg?): Boolean {
                        return false
                    }

                    override fun callBack(p0: com.wellcom.IdCardMsg?): Boolean {
                        return false
                    }
                }
                val idCardMsg = bGSdtUtil?.readCard(Utils.getApp(), idCardCallback, false)
                if (idCardMsg != null && idCardMsg.isReadSucc && !StringUtils.isEmpty(idCardMsg.id_num)) {
                    hidFlag = false
                    it.onNext(idCardMsg)
                    it.onComplete()
                }
            }
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner)))
            .subscribe({
                val cardInfo = CardInfo()
                cardInfo.address = it.address?.trim()
                cardInfo.card = it.id_num?.trim()
                cardInfo.name = it.name?.trim()
                cardInfo.nation = it.nation_str?.trim()
                cardInfo.sex = it.sex?.trim()
                cardInfo.bm = it.ptoto
                idCardListener?.idCardListener(cardInfo)
            },
                { it.printStackTrace() })
    }

    fun stopReadIdCard() {
        weFlag = false
        hidFlag = false
        bGSdtUtil?.stopReadIdCard(false)
    }

    class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            if (UsbManager.ACTION_USB_DEVICE_DETACHED == action) {  // USB设备拔出广播
                val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                val deviceName = device?.deviceName ?: ""
                if (device?.equals(deviceName)!!) {
                    ToastUtils.showLong("USB设备拔出，应用程序即将关闭")
                    ActivityUtils.getActivityByContext(context).finish()
                }
            } else {
//                ToastUtils.showLong("USB设备无权限")
                LogUtils.d("USB设备无权限")
            }
        }
    }

}