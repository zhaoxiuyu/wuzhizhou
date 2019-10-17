package com.sendinfo.wuzhizhou.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.base.library.database.DataBaseUtils
import com.base.library.entitys.BaseResponse
import com.base.library.http.BRequest
import com.base.library.util.JsonUtils
import com.base.library.util.roomInsertJournalRecord
import com.blankj.utilcode.constant.TimeConstants
import com.blankj.utilcode.util.BusUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.TimeUtils
import com.sendinfo.wuzhizhou.entitys.response.BeatVo
import com.sendinfo.wuzhizhou.utils.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

/**
 * 心跳
 */
class BeatService : Service() {

    override fun onCreate() {
        super.onCreate()

        // 删除7天前的日志记录
        DataBaseUtils.getJournalRecordDao()
            .deleteFormTime(TimeUtils.getString(TimeUtils.getNowString(), -7, TimeConstants.DAY))
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                LogUtils.d("删除了多少条:$it")
            }, Consumer {
                LogUtils.e("删除: $it.localizedMessage")
            })

        // 两分钟调用一次
        Observable.interval(0, 60, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (!StringUtils.isEmpty(getIp())) {
                    httpBeat()
                }
            }
    }

    private fun httpBeat() {
        val map = mapOf("terminalCode" to getShebeiCode())
        val bRequest = BRequest(Beat).apply {
            httpType = BRequest.GET
            params = map
        }
        bRequest.print()
        bRequest.getOkRx2()
            .subscribeOn(AndroidSchedulers.mainThread())
            .map {
                val response = JsonUtils.toAny(it, BaseResponse::class.java)
                if (response.success) {
                    JsonUtils.toAny(response.data ?: "", BeatVo::class.java)
                } else {
                    BeatVo()
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                if (!StringUtils.isEmpty(it.BeginTime)) {
                    val sb = StringBuilder()
                    sb.appendln("开始时间:${it.BeginTime}  结束时间:${it.EndTime}  ")

                    val beginTime1 = TimeUtils.getTimeSpanByNow(it.BeginTime, TimeConstants.SEC)
                    val endTime1 = TimeUtils.getTimeSpanByNow(it.EndTime, TimeConstants.SEC)

                    sb.appendln("  计算之后得到的数据 beginTime1=$beginTime1  --  endTime1=$endTime1  ")

                    // 当前时间在 开始 和 结束 时间之间
                    if (beginTime1 < 0 && endTime1 > 0) {
                        sb.appendln("  自助机正常")
                        BusUtils.post(StartZZJ, "自助机正常")
                    } else {
                        // 把yyyy-MM-dd HH:mm:ss 时间字符串转为data,然后在转为yyyy-MM-dd HH:mm 字符串, 去掉了秒
                        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
                        val beginData = TimeUtils.string2Date(it.BeginTime, sdf)
                        val endData = TimeUtils.string2Date(it.EndTime, sdf)

                        BusUtils.post(
                            StopZZJ,
                            "服务时间段为 \n ${TimeUtils.date2String(beginData, sdf)}" +
                                    " 至  ${TimeUtils.date2String(endData, sdf)}"
                        )
                    }

                    LogUtils.d("$sb")
                    addRz("$sb", "${bRequest.url}心跳返回")
                }
            }, Consumer {
                LogUtils.e(it.message)
                addRz("${it.message}", "${bRequest.url}异常-心跳")
            })
    }

    private fun addRz(message: String, behavior: String) {
        roomInsertJournalRecord("$message", "$behavior", "I")
            .subscribe(Consumer { LogUtils.d("日志添加成功") }, Consumer { LogUtils.e("日志添加失败") })
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}