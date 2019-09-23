package com.dolan.dolantancepapp.alarm

import java.util.concurrent.atomic.AtomicInteger

class NotificationID {
    companion object {
        private val atomicInteger = AtomicInteger(0)
        fun getID(): Int {
            return atomicInteger.incrementAndGet()
        }
    }
}