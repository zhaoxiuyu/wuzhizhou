package com.sendinfo.wuzhizhou.owner

import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.base.library.interfaces.MyLifecycleObserver
import com.sendinfo.padserialhelp.SerialHelper

/**
 * 二维码扫描
 */
class QrCodeOwner(private val sendPort: String, private var handler: Handler?) : Thread(), MyLifecycleObserver {

    private lateinit var owner: LifecycleOwner

    override fun onCreate(owner: LifecycleOwner) {
        this.owner = owner
    }

    override fun onDestroy(owner: LifecycleOwner) {
        stopReadSysCan()
    }

    fun startReadSysCan() {
        start()
    }

    fun stopReadSysCan() {
        interrupt()
        handler?.removeCallbacksAndMessages(null)
        handler = null
    }

    override fun run() {
        super.run()
        try {
            val oldTime = System.currentTimeMillis()
            val serialPort = SerialHelper.getInstance().getGmSerialPort(sendPort)

            val inputStream = serialPort.inputStream
            if (inputStream != null) {
                if (inputStream.available() > 0) {
                    inputStream.read()
                }
                val bufferBytes = ByteArray(1024)
                var totalSize = 0

                var readFlag = false
                var completeRead = 0
                while (!Thread.interrupted()) {
                    val size: Int

                    if (inputStream.available() <= 0) {
                        Thread.sleep(100)
                        continue
                    }
                    val tmpBuffer = ByteArray(128)
                    size = inputStream.read(tmpBuffer)
                    Log.e("size", "$size")
                    var str = ""
                    for (i in 0 until size) {
                        str += tmpBuffer[i]
                    }
                    Log.e("tmpBuffer", str)
                    Log.e("tmpBuffer", str)
                    var startIndex = 0
                    var readCount = 0
                    for (i in 0 until size) {

                        val itembyte = tmpBuffer[i]

                        if (itembyte.toInt() == 13) {
                            readFlag = false
                            completeRead += 1
                            Log.e("读取结束", "即将结束")

                        }
                        if (itembyte.toInt() == 10) {
                            readFlag = false
                            completeRead += 1
                            Log.e("读取结束", "结束")
                            break
                        }

                        if (readFlag) {
                            readCount++
                        }
                        if (itembyte.toInt() == -86) {
                            startIndex = i + 1
                            //                            readCount++;
                            Log.e("读取开始", "开始")
                            readFlag = true
                            completeRead = 0
                        }

                    }
                    if (readCount > 0) {
                        System.arraycopy(
                            tmpBuffer, startIndex, bufferBytes,
                            totalSize, readCount
                        )
                    }
                    totalSize += readCount
                    var resultstr = ""
                    for (i in 0 until readCount) {
                        resultstr += bufferBytes[i]
                    }
                    Log.e("bufferBytes", resultstr)
                    if (!Thread.interrupted() && completeRead == 2 && totalSize > 0) {

                        val result = String(bufferBytes, 0, totalSize)
                        Log.e("result", result)
                        val msg = Message.obtain()
                        msg.what = 1
                        msg.obj = result
                        handler?.sendMessage(msg)
                        interrupt()
                    }
                }
            }
        } catch (e: Exception) {
            val msg = Message.obtain()
            msg.what = 0
            msg.obj = e.toString()
            handler?.sendMessage(msg)
        }
    }

}