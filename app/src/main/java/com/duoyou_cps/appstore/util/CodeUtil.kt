package com.duoyou_cps.appstore.util

import android.os.CountDownTimer
import android.widget.TextView

class CodeUtil(var textView: TextView, var time: Int) {
        private var countDownTimer: CountDownTimer? = null

        fun startCountDown() {
            countDownTimer?.cancel()
            textView.isEnabled = false
            countDownTimer = object : CountDownTimer(time * 1000L, 1000L) {
                override fun onTick(millisUntilFinished: Long) {
                    textView.text = String.format("%sS", (millisUntilFinished + 999L) / 1000)
                    textView.isEnabled = false
                }

                override fun onFinish() {
                    textView.isEnabled = true
                    textView.text = "获取"
                }
            }
            countDownTimer?.start()
        }

        fun onDestroy() {
            countDownTimer?.cancel()
            countDownTimer = null
        }
    }