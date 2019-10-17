package com.sendinfo.wuzhizhou.entitys.hardware

import com.sendinfo.honeywellprintlib.print.PrintReturnDto

/**
 * 备注：打印进度
 * progress   进度
 * total      打印数组总长度
 * isComplete 是否完成所有的打印任务
 * succ 打印机状态
 * errorMsg 打印机异常信息
 */
class PrintProgress {
    var progress: Int = 0
    var total: Int = 0
    var isComplete: Boolean = false
    var succ: Boolean = false
    var errorMsg: String? = null
    var printReturnDto: PrintReturnDto? = null
    var tscPrintReturnDto: com.sendinfo.tscprintlib.PrintReturnDto? = null
    var temp: String = ""

    override fun toString(): String {
        return "PrintProgress(progress=$progress, total=$total, isComplete=$isComplete, succ=$succ, errorMsg=$errorMsg, printReturnDto=$printReturnDto, tscPrintReturnDto=$tscPrintReturnDto, temp='$temp')"
    }

}
