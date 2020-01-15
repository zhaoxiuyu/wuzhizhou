package com.base.library.database

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.base.library.http.BRequest
import com.base.library.mvp.BPresenter

/**
 * 数据库增删改查工具类，生命周期自动管理
 */
class RoomLifecycle : BPresenter {

    private lateinit var owner: LifecycleOwner

    override fun getData(http: BRequest) {
    }

    override fun onCreate(owner: LifecycleOwner) {
        this.owner = owner
    }

    override fun onResume(owner: LifecycleOwner) {
    }

    override fun onDestroy(owner: LifecycleOwner) {
    }

    override fun onLifecycleChanged(owner: LifecycleOwner, event: Lifecycle.Event) {
    }

    // 添加日志
//    fun addJournal(journalRecord: JournalRecord) {
//        DataBaseUtils.getJournalRecordDao().insertRxCompletable(journalRecord)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner)))
//            .subscribe(Consumer {
//                LogUtils.d("插入的主键是:$it")
//            }, Consumer {
//                LogUtils.e("删除:$it.localizedMessage")
//            })
//    }

    // 删除多少天之前的日志,默认7天
//    fun removeBefore(day: Long = 7) {
//        // 7天前的日期
//        val day7 = TimeUtils.getString(TimeUtils.getNowString(), -day, TimeConstants.DAY)
//        DataBaseUtils.getJournalRecordDao().deleteFormTime(day7)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner)))
//            .subscribe(Consumer {
//                LogUtils.d("删除了多少条:$it")
//            }, Consumer {
//                LogUtils.e("删除: $it.localizedMessage")
//            })
//    }

}
