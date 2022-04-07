package com.duoyou_cps.appstore.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import java.util.*

class User2 : BaseObservable() {
    private var name: String? = null

    @Bindable
    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
        notifyChange()
    }
}